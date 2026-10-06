package com.campuslink.complaint;

import com.campuslink.audit.AsyncActivityLogService;
import com.campuslink.common.exception.ApiException;
import com.campuslink.complaint.dto.*;
import com.campuslink.notification.NotificationService;
import com.campuslink.user.Role;
import com.campuslink.user.User;
import com.campuslink.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ComplaintService {
    private final ComplaintPostRepository postRepo;
    private final ComplaintCommentRepository commentRepo;
    private final ComplaintReactionRepository reactionRepo;
    private final UserService users;
    private final NotificationService notifications;
    private final AsyncActivityLogService activityLogs;

    public ComplaintService(ComplaintPostRepository p, ComplaintCommentRepository c, ComplaintReactionRepository r,
            UserService u, NotificationService n, AsyncActivityLogService a) {
        postRepo = p;
        commentRepo = c;
        reactionRepo = r;
        users = u;
        notifications = n;
        activityLogs = a;
    }

    public List<ComplaintResponse> all() {
        return postRepo.findAllByOrderByCreatedAtDesc().stream().filter(p -> p.getStatus() != ComplaintStatus.REMOVED)
                .map(this::toResponse).toList();
    }

    public ComplaintResponse get(UUID id) {
        return toResponse(find(id));
    }

    public List<ComplaintResponse> mine() {
        return postRepo.findByAuthorOrderByCreatedAtDesc(users.getCurrentUser()).stream().map(this::toResponse)
                .toList();
    }

    @Transactional
    public ComplaintResponse create(ComplaintInput i) {
        User u = users.getCurrentUser();
        if (u.getRole() == Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "Admin should not create community complaint posts.");
        ComplaintPost p = new ComplaintPost();
        p.setAuthor(u);
        apply(p, i);
        ComplaintPost saved = postRepo.save(p);
        activityLogs.record(u.getId(), u.getEmail(), "COMPLAINT_CREATE", saved.getTitle());
        return toResponse(saved);
    }

    @Transactional
    public ComplaintResponse update(UUID id, ComplaintInput i) {
        ComplaintPost p = find(id);
        requireOwner(p);
        apply(p, i);
        return toResponse(postRepo.save(p));
    }

    @Transactional
    public void remove(UUID id) {
        ComplaintPost p = find(id);
        User u = users.getCurrentUser();
        if (!p.getAuthor().getId().equals(u.getId()) && u.getRole() != Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this complaint.");
        p.setStatus(ComplaintStatus.REMOVED);
        postRepo.save(p);
    }

    public List<ComplaintCommentResponse> comments(UUID id) {
        ComplaintPost p = find(id);
        return commentRepo.findByPostAndRemovedFalseOrderByCreatedAtAsc(p).stream().map(ComplaintCommentResponse::from)
                .toList();
    }

    @Transactional
    public ComplaintCommentResponse comment(UUID id, ComplaintCommentInput i) {
        ComplaintPost p = find(id);
        ComplaintComment c = new ComplaintComment();
        c.setPost(p);
        c.setAuthor(users.getCurrentUser());
        c.setText(i.text());
        return ComplaintCommentResponse.from(commentRepo.save(c));
    }

    @Transactional
    public void removeComment(UUID id) {
        ComplaintComment c = commentRepo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Comment not found."));
        User u = users.getCurrentUser();
        if (!c.getAuthor().getId().equals(u.getId()) && u.getRole() != Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this comment.");
        c.setRemoved(true);
        commentRepo.save(c);
    }

    @Transactional
    public ComplaintResponse toggleReaction(UUID id) {
        ComplaintPost p = find(id);
        User u = users.getCurrentUser();
        Optional<ComplaintReaction> ex = reactionRepo.findByPostAndUser(p, u);
        if (ex.isPresent())
            reactionRepo.delete(ex.get());
        else {
            ComplaintReaction r = new ComplaintReaction();
            r.setPost(p);
            r.setUser(u);
            reactionRepo.save(r);
        }
        return toResponse(p);
    }

    @Transactional
    public ComplaintResponse adminUpdate(UUID id, AdminComplaintUpdate i) {
        ComplaintPost p = find(id);
        p.setStatus(i.status());
        p.setOfficialResponse(i.officialResponse());
        ComplaintPost saved = postRepo.save(p);
        notifications.create(p.getAuthor(),
                "Your complaint '" + p.getTitle() + "' is now " + i.status().name().toLowerCase() + ".",
                "/complaints/" + p.getId());
        User admin = users.getCurrentUser();
        activityLogs.record(admin.getId(), admin.getEmail(), "COMPLAINT_ADMIN_UPDATE",
                saved.getTitle() + " -> " + saved.getStatus());
        return toResponse(saved);
    }

    private ComplaintPost find(UUID id) {
        return postRepo.findById(id).orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Complaint not found."));
    }

    private void requireOwner(ComplaintPost p) {
        if (!p.getAuthor().getId().equals(users.getCurrentUser().getId()))
            throw new ApiException(HttpStatus.FORBIDDEN, "You can edit only your own complaint.");
    }

    private void apply(ComplaintPost p, ComplaintInput i) {
        p.setTitle(i.title());
        p.setContent(i.content());
        p.setImageUrl(i.imageUrl());
        p.setVideoUrl(i.videoUrl());
    }

    private ComplaintResponse toResponse(ComplaintPost p) {
        return ComplaintResponse.from(p, reactionRepo.countByPost(p));
    }
}
