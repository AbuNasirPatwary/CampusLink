package com.campuslink.borrowhub;

import com.campuslink.audit.AsyncActivityLogService;
import com.campuslink.borrowhub.dto.*;
import com.campuslink.common.exception.ApiException;
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
public class BorrowHubService {
    private final BorrowRequestRepository requestRepository;
    private final BorrowCommentRepository commentRepository;
    private final UserService userService;
    private final NotificationService notifications;
    private final AsyncActivityLogService activityLogs;

    public BorrowHubService(BorrowRequestRepository requestRepository, BorrowCommentRepository commentRepository,
            UserService userService, NotificationService notifications, AsyncActivityLogService activityLogs) {
        this.requestRepository = requestRepository;
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.notifications = notifications;
        this.activityLogs = activityLogs;
    }

    public List<BorrowRequestResponse> all() {
        return requestRepository.findAllByOrderByCreatedAtDesc().stream()
                .filter(r -> r.getStatus() != BorrowStatus.REMOVED).map(BorrowRequestResponse::from).toList();
    }

    public BorrowRequestResponse get(UUID id) {
        return BorrowRequestResponse.from(find(id));
    }

    public List<BorrowRequestResponse> mine() {
        return requestRepository.findByAuthorOrderByCreatedAtDesc(userService.getCurrentUser()).stream()
                .map(BorrowRequestResponse::from).toList();
    }

    @Transactional
    public BorrowRequestResponse create(BorrowRequestInput input) {
        User user = normalUser();
        BorrowRequest r = new BorrowRequest();
        r.setAuthor(user);
        apply(r, input);
        r.setStatus(BorrowStatus.OPEN);
        BorrowRequest saved = requestRepository.save(r);
        activityLogs.record(user.getId(), user.getEmail(), "BORROWHUB_CREATE", saved.getTitle());
        return BorrowRequestResponse.from(saved);
    }

    @Transactional
    public BorrowRequestResponse update(UUID id, BorrowRequestInput input) {
        BorrowRequest r = find(id);
        requireOwner(r);
        apply(r, input);
        return BorrowRequestResponse.from(requestRepository.save(r));
    }

    @Transactional
    public BorrowRequestResponse complete(UUID id) {
        BorrowRequest r = find(id);
        requireOwner(r);
        r.setStatus(BorrowStatus.COMPLETED);
        BorrowRequest saved = requestRepository.save(r);
        User user = userService.getCurrentUser();
        activityLogs.record(user.getId(), user.getEmail(), "BORROWHUB_COMPLETE", saved.getTitle());
        return BorrowRequestResponse.from(saved);
    }

    @Transactional
    public void remove(UUID id) {
        BorrowRequest r = find(id);
        User current = userService.getCurrentUser();
        if (!r.getAuthor().getId().equals(current.getId()) && current.getRole() != Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this request.");
        r.setStatus(BorrowStatus.REMOVED);
        requestRepository.save(r);
    }

    public List<BorrowCommentResponse> comments(UUID requestId) {
        BorrowRequest r = find(requestId);
        return commentRepository.findByRequestAndRemovedFalseOrderByCreatedAtAsc(r).stream()
                .map(BorrowCommentResponse::from).toList();
    }

    @Transactional
    public BorrowCommentResponse comment(UUID requestId, BorrowCommentInput input) {
        User user = normalUser();
        BorrowRequest r = find(requestId);
        if (r.getStatus() != BorrowStatus.OPEN)
            throw new ApiException(HttpStatus.BAD_REQUEST, "Comments are closed for this request.");
        BorrowComment c = new BorrowComment();
        c.setRequest(r);
        c.setAuthor(user);
        c.setText(input.text());
        BorrowComment saved = commentRepository.save(c);
        if (!r.getAuthor().getId().equals(user.getId()))
            notifications.create(r.getAuthor(), user.getFullName() + " commented on your BorrowHub request.",
                    "/borrowhub/" + r.getId());
        return BorrowCommentResponse.from(saved);
    }

    @Transactional
    public void removeComment(UUID id) {
        BorrowComment c = commentRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Comment not found."));
        User current = userService.getCurrentUser();
        if (!c.getAuthor().getId().equals(current.getId()) && current.getRole() != Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this comment.");
        c.setRemoved(true);
        commentRepository.save(c);
    }

    private User normalUser() {
        User u = userService.getCurrentUser();
        if (u.getRole() != Role.STUDENT && u.getRole() != Role.ALUMNI)
            throw new ApiException(HttpStatus.FORBIDDEN, "BorrowHub is available to students and approved alumni.");
        return u;
    }

    private BorrowRequest find(UUID id) {
        return requestRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "BorrowHub request not found."));
    }

    private void requireOwner(BorrowRequest r) {
        if (!r.getAuthor().getId().equals(userService.getCurrentUser().getId()))
            throw new ApiException(HttpStatus.FORBIDDEN, "You can edit only your own request.");
    }

    private void apply(BorrowRequest r, BorrowRequestInput i) {
        r.setTitle(i.title());
        r.setDescription(i.description());
        r.setImageUrl(i.imageUrl());
        r.setRequiredDate(i.requiredDate());
        r.setExpectedReturnDate(i.expectedReturnDate());
    }
}
