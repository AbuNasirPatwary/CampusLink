package com.campuslink.talent;

import com.campuslink.audit.AsyncActivityLogService;
import com.campuslink.common.exception.ApiException;
import com.campuslink.talent.dto.*;
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
public class TalentService {
    private final TalentPostRepository postRepo;
    private final TalentCommentRepository commentRepo;
    private final TalentReactionRepository reactionRepo;
    private final TalentBookmarkRepository bookmarkRepo;
    private final UserService users;
    private final NotificationService notifications;
    private final AsyncActivityLogService activityLogs;

    public TalentService(TalentPostRepository p, TalentCommentRepository c, TalentReactionRepository r,
            TalentBookmarkRepository b, UserService u, NotificationService n, AsyncActivityLogService a) {
        postRepo = p;
        commentRepo = c;
        reactionRepo = r;
        bookmarkRepo = b;
        users = u;
        notifications = n;
        activityLogs = a;
    }

    public List<TalentPostResponse> all() {
        return postRepo.findAllByOrderByCreatedAtDesc().stream().filter(p -> p.getStatus() != TalentStatus.REMOVED)
                .map(this::toResponse).toList();
    }

    public TalentPostResponse get(UUID id) {
        return toResponse(find(id));
    }

    public List<TalentPostResponse> mine() {
        return postRepo.findByAuthorOrderByCreatedAtDesc(users.getCurrentUser()).stream().map(this::toResponse)
                .toList();
    }

    @Transactional
    public TalentPostResponse create(TalentPostInput i) {
        User u = users.getCurrentUser();
        if (u.getRole() == Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "Admin should not create TalentHub posts.");
        validate(i);
        TalentPost p = new TalentPost();
        p.setAuthor(u);
        apply(p, i);
        TalentPost saved = postRepo.save(p);
        activityLogs.record(u.getId(), u.getEmail(), "TALENT_CREATE", saved.getCategory() + ": " + saved.getTitle());
        return toResponse(saved);
    }

    @Transactional
    public TalentPostResponse update(UUID id, TalentPostInput i) {
        TalentPost p = find(id);
        requireOwner(p);
        validate(i);
        apply(p, i);
        return toResponse(postRepo.save(p));
    }

    @Transactional
    public void remove(UUID id) {
        TalentPost p = find(id);
        User u = users.getCurrentUser();
        if (!p.getAuthor().getId().equals(u.getId()) && u.getRole() != Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this post.");
        p.setStatus(TalentStatus.REMOVED);
        postRepo.save(p);
        if (u.getRole() == Role.ADMIN) {
            activityLogs.record(u.getId(), u.getEmail(), "TALENT_ADMIN_REMOVE", p.getTitle());
        }
    }

    public List<TalentCommentResponse> comments(UUID id) {
        TalentPost p = find(id);
        return commentRepo.findByPostAndRemovedFalseOrderByCreatedAtAsc(p).stream().map(TalentCommentResponse::from)
                .toList();
    }

    @Transactional
    public TalentCommentResponse comment(UUID id, TalentCommentInput i) {
        TalentPost p = find(id);
        User author = users.getCurrentUser();
        TalentComment c = new TalentComment();
        c.setPost(p);
        c.setAuthor(author);
        c.setText(i.text());
        TalentComment saved = commentRepo.save(c);
        if (!p.getAuthor().getId().equals(author.getId()))
            notifications.create(p.getAuthor(), author.getFullName() + " commented on your TalentHub post.",
                    "/talent/" + p.getId());
        return TalentCommentResponse.from(saved);
    }

    @Transactional
    public void removeComment(UUID id) {
        TalentComment c = commentRepo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Comment not found."));
        User u = users.getCurrentUser();
        if (!c.getAuthor().getId().equals(u.getId()) && u.getRole() != Role.ADMIN)
            throw new ApiException(HttpStatus.FORBIDDEN, "You cannot remove this comment.");
        c.setRemoved(true);
        commentRepo.save(c);
    }

    @Transactional
    public TalentPostResponse toggleLike(UUID id) {
        TalentPost p = find(id);
        User u = users.getCurrentUser();
        Optional<TalentReaction> ex = reactionRepo.findByPostAndUser(p, u);
        if (ex.isPresent())
            reactionRepo.delete(ex.get());
        else {
            TalentReaction r = new TalentReaction();
            r.setPost(p);
            r.setUser(u);
            reactionRepo.save(r);
        }
        return toResponse(p);
    }

    @Transactional
    public boolean toggleBookmark(UUID id) {
        TalentPost p = find(id);
        User u = users.getCurrentUser();
        Optional<TalentBookmark> ex = bookmarkRepo.findByPostAndUser(p, u);
        if (ex.isPresent()) {
            bookmarkRepo.delete(ex.get());
            return false;
        }
        TalentBookmark b = new TalentBookmark();
        b.setPost(p);
        b.setUser(u);
        bookmarkRepo.save(b);
        return true;
    }

    public List<TalentPostResponse> bookmarks() {
        return bookmarkRepo.findByUserOrderByCreatedAtDesc(users.getCurrentUser()).stream().map(TalentBookmark::getPost)
                .filter(p -> p.getStatus() != TalentStatus.REMOVED).map(this::toResponse).toList();
    }

    @Transactional
    public TalentPostResponse feature(UUID id, boolean featured) {
        TalentPost p = find(id);
        p.setFeatured(featured);
        TalentPost saved = postRepo.save(p);
        User admin = users.getCurrentUser();
        activityLogs.record(admin.getId(), admin.getEmail(), featured ? "TALENT_FEATURE" : "TALENT_UNFEATURE", saved.getTitle());
        return toResponse(saved);
    }

    private TalentPost find(UUID id) {
        return postRepo.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "TalentHub post not found."));
    }

    private void requireOwner(TalentPost p) {
        if (!p.getAuthor().getId().equals(users.getCurrentUser().getId()))
            throw new ApiException(HttpStatus.FORBIDDEN, "You can edit only your own post.");
    }

    private void apply(TalentPost p, TalentPostInput i) {
        p.setCategory(i.category());
        p.setTitle(i.title());
        p.setContent(i.content());
        p.setMediaUrl(i.mediaUrl());
        p.setThumbnailUrl(i.thumbnailUrl());
        p.setLocation(i.location());
    }

    private void validate(TalentPostInput i) {
        if (i.category() == TalentCategory.WRITING && (i.content() == null || i.content().isBlank()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "Writing posts require written content.");
        if (i.category() != TalentCategory.WRITING && (i.mediaUrl() == null || i.mediaUrl().isBlank()))
            throw new ApiException(HttpStatus.BAD_REQUEST, "This TalentHub category requires a media file.");
    }

    private TalentPostResponse toResponse(TalentPost p) {
        return TalentPostResponse.from(p, reactionRepo.countByPost(p));
    }
}
