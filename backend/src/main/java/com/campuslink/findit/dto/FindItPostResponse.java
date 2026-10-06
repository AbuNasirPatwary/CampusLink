package com.campuslink.findit.dto;

import com.campuslink.findit.FindItPost;
import com.campuslink.findit.FindItType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Student/alumni-facing FindIt DTO.
 * FOUND verification images are deliberately omitted so they cannot leak
 * through normal API responses. LOST images remain public when provided.
 */
public record FindItPostResponse(
        UUID id,
        UUID ownerId,
        String ownerName,
        String type,
        String title,
        String description,
        String location,
        LocalDate eventDate,
        String imageUrl,
        String status,
        LocalDateTime createdAt
) {
    public static FindItPostResponse from(FindItPost p) {
        String publicImage = p.getType() == FindItType.LOST ? p.getImageUrl() : null;
        return new FindItPostResponse(
                p.getId(),
                p.getOwner().getId(),
                p.getOwner().getFullName(),
                p.getType().name(),
                p.getTitle(),
                p.getDescription(),
                p.getLocation(),
                p.getEventDate(),
                publicImage,
                p.getStatus().name(),
                p.getCreatedAt()
        );
    }
}
