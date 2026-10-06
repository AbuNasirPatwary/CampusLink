package com.campuslink.borrowhub.dto;
import com.campuslink.borrowhub.BorrowComment;
import java.time.LocalDateTime;
import java.util.UUID;
public record BorrowCommentResponse(UUID id, UUID authorId, String authorName, String authorEmail,
                                    String text, LocalDateTime createdAt) {
    public static BorrowCommentResponse from(BorrowComment c) {
        return new BorrowCommentResponse(c.getId(), c.getAuthor().getId(), c.getAuthor().getFullName(),
                c.getAuthor().getEmail(), c.getText(), c.getCreatedAt());
    }
}
