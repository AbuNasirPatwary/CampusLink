package com.campuslink.findit;

import com.campuslink.audit.AsyncActivityLogService;
import com.campuslink.common.exception.ApiException;
import com.campuslink.findit.dto.*;
import com.campuslink.notification.NotificationService;
import com.campuslink.user.Role;
import com.campuslink.user.User;
import com.campuslink.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class FindItService {
    private final FindItPostRepository postRepository;
    private final ItemClaimRepository claimRepository;
    private final UserService userService;
    private final NotificationService notifications;
    private final AsyncActivityLogService activityLogs;

    public FindItService(FindItPostRepository postRepository, ItemClaimRepository claimRepository,
            UserService userService, NotificationService notifications, AsyncActivityLogService activityLogs) {
        this.postRepository = postRepository;
        this.claimRepository = claimRepository;
        this.userService = userService;
        this.notifications = notifications;
        this.activityLogs = activityLogs;
    }

    public List<FindItPostResponse> all() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(p -> p.getStatus() != FindItStatus.REMOVED)
                .map(FindItPostResponse::from)
                .toList();
    }

    public FindItPostResponse get(UUID id) {
        return FindItPostResponse.from(findPost(id));
    }

    public List<FindItPostResponse> mine() {
        User user = normalUser();
        return postRepository.findByOwnerOrderByCreatedAtDesc(user).stream()
                .map(FindItPostResponse::from)
                .toList();
    }

    @Transactional
    public FindItPostResponse create(FindItPostRequest request) {
        User user = normalUser();
        validatePhotoRules(request);
        FindItPost post = new FindItPost();
        apply(post, request);
        post.setOwner(user);
        post.setStatus(FindItStatus.OPEN);
        FindItPost saved = postRepository.save(post);
        activityLogs.record(user.getId(), user.getEmail(), "FINDIT_CREATE", saved.getType() + ": " + saved.getTitle());
        return FindItPostResponse.from(saved);
    }

    @Transactional
    public FindItPostResponse update(UUID id, FindItPostRequest request) {
        FindItPost post = findPost(id);
        requireOwner(post);
        if (post.getStatus() == FindItStatus.DELIVERED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Delivered posts cannot be edited.");
        }
        validatePhotoRules(request);
        apply(post, request);
        return FindItPostResponse.from(postRepository.save(post));
    }

    @Transactional
    public void remove(UUID id) {
        FindItPost post = findPost(id);
        User current = userService.getCurrentUser();
        if (!post.getOwner().getId().equals(current.getId()) && current.getRole() != Role.ADMIN) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this post.");
        }
        post.setStatus(FindItStatus.REMOVED);
        postRepository.save(post);
        activityLogs.record(current.getId(), current.getEmail(), "FINDIT_REMOVE", "Removed post " + post.getId());
    }

    @Transactional
    public ClaimResponse claim(UUID postId, ClaimRequest request) {
        User user = normalUser();
        FindItPost post = findPost(postId);
        if (post.getType() != FindItType.FOUND) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Claims are submitted only for found-item posts.");
        }
        if (post.getStatus() != FindItStatus.OPEN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "This item is no longer open for claims.");
        }
        if (post.getOwner().getId().equals(user.getId())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot claim your own post.");
        }
        if (claimRepository.existsByPostAndClaimantAndStatus(post, user, ClaimStatus.PENDING)) {
            throw new ApiException(HttpStatus.CONFLICT, "You already have a pending claim for this item.");
        }
        ItemClaim claim = new ItemClaim();
        claim.setPost(post);
        claim.setClaimant(user);
        claim.setOwnershipDescription(request.ownershipDescription().trim());
        ItemClaim saved = claimRepository.save(claim);
        activityLogs.record(user.getId(), user.getEmail(), "FINDIT_CLAIM", "Claimed post " + post.getId());
        return ClaimResponse.from(saved);
    }

    public List<ClaimResponse> myClaims() {
        User current = normalUser();
        return claimRepository.findByClaimantOrderByCreatedAtDesc(current).stream()
                .map(ClaimResponse::from)
                .toList();
    }

    public List<AdminClaimResponse> allClaims() {
        return claimRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(AdminClaimResponse::from)
                .toList();
    }

    @Transactional
    public AdminClaimResponse reviewClaim(UUID claimId, ClaimStatus status) {
        if (status == ClaimStatus.PENDING) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Choose APPROVED or REJECTED.");
        }
        ItemClaim claim = claimRepository.findById(claimId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Claim not found."));
        if (claim.getPost().getStatus() == FindItStatus.DELIVERED) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Delivered items cannot be reviewed again.");
        }

        claim.setStatus(status);
        FindItPost post = claim.getPost();
        if (status == ClaimStatus.APPROVED) {
            post.setStatus(FindItStatus.CLAIMED);
            for (ItemClaim other : claimRepository.findByPostAndStatus(post, ClaimStatus.PENDING)) {
                if (!other.getId().equals(claim.getId())) {
                    other.setStatus(ClaimStatus.REJECTED);
                    claimRepository.save(other);
                    notifications.create(other.getClaimant(),
                            "Your FindIt claim for '" + post.getTitle() + "' was rejected because another ownership claim was approved.",
                            "/findit/" + post.getId());
                }
            }
        } else if (post.getStatus() != FindItStatus.CLAIMED) {
            post.setStatus(FindItStatus.OPEN);
        }

        claimRepository.save(claim);
        postRepository.save(post);
        notifications.create(claim.getClaimant(),
                "Your FindIt claim for '" + post.getTitle() + "' was " + status.name().toLowerCase() + ".",
                "/findit/" + post.getId());
        User admin = userService.getCurrentUser();
        activityLogs.record(admin.getId(), admin.getEmail(), "FINDIT_CLAIM_REVIEW", status + " claim " + claim.getId());
        return AdminClaimResponse.from(claim);
    }

    @Transactional
    public FindItPostResponse markDelivered(UUID postId) {
        FindItPost post = findPost(postId);
        List<ItemClaim> approvedClaims = claimRepository.findByPostAndStatus(post, ClaimStatus.APPROVED);
        if (approvedClaims.isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Approve an ownership claim before marking the item delivered.");
        }
        post.setStatus(FindItStatus.DELIVERED);
        FindItPost saved = postRepository.save(post);
        ItemClaim approved = approvedClaims.get(0);
        notifications.create(approved.getClaimant(),
                "Your FindIt item '" + post.getTitle() + "' has been marked delivered.",
                "/findit/" + post.getId());
        User admin = userService.getCurrentUser();
        activityLogs.record(admin.getId(), admin.getEmail(), "FINDIT_DELIVERED", "Delivered post " + postId);
        return FindItPostResponse.from(saved);
    }

    private FindItPost findPost(UUID id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "FindIt post not found."));
    }

    private User normalUser() {
        User user = userService.getCurrentUser();
        if (user.getRole() != Role.STUDENT && user.getRole() != Role.ALUMNI) {
            throw new ApiException(HttpStatus.FORBIDDEN, "This action is available to students and approved alumni.");
        }
        return user;
    }

    private void requireOwner(FindItPost post) {
        if (!post.getOwner().getId().equals(userService.getCurrentUser().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "You can edit only your own post.");
        }
    }

    private void validatePhotoRules(FindItPostRequest request) {
        if (request.type() == FindItType.FOUND && (request.imageUrl() == null || request.imageUrl().isBlank())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "A private verification photo is required for a found-item post.");
        }
    }

    private void apply(FindItPost post, FindItPostRequest request) {
        post.setType(request.type());
        post.setTitle(request.title().trim());
        post.setDescription(request.description().trim());
        post.setLocation(request.location().trim());
        post.setEventDate(request.eventDate());
        post.setImageUrl(request.imageUrl());
    }
}
