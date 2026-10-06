package com.campuslink.borrowhub.dto;
import com.campuslink.borrowhub.BorrowRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
public record BorrowRequestResponse(UUID id, UUID authorId, String authorName, String authorEmail, String title,
                                    String description, String imageUrl, LocalDate requiredDate,
                                    LocalDate expectedReturnDate, String status, LocalDateTime createdAt) {
    public static BorrowRequestResponse from(BorrowRequest r) {
        return new BorrowRequestResponse(r.getId(), r.getAuthor().getId(), r.getAuthor().getFullName(), r.getAuthor().getEmail(),
                r.getTitle(), r.getDescription(), r.getImageUrl(), r.getRequiredDate(), r.getExpectedReturnDate(),
                r.getStatus().name(), r.getCreatedAt());
    }
}
