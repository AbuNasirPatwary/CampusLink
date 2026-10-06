package com.campuslink.findit.dto;

import com.campuslink.findit.ItemClaim;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/** Admin-only claim view. verificationImageUrl is an internal private-media key. */
public record AdminClaimResponse(
        UUID id,
        UUID postId,
        String postTitle,
        String postDescription,
        String postLocation,
        LocalDate postEventDate,
        String postStatus,
        String foundByName,
        String verificationImageUrl,
        UUID claimantId,
        String claimantName,
        String claimantStudentId,
        String claimantEmail,
        String ownershipDescription,
        String status,
        LocalDateTime createdAt
) {
    public static AdminClaimResponse from(ItemClaim c) {
        return new AdminClaimResponse(
                c.getId(), c.getPost().getId(), c.getPost().getTitle(), c.getPost().getDescription(),
                c.getPost().getLocation(), c.getPost().getEventDate(), c.getPost().getStatus().name(),
                c.getPost().getOwner().getFullName(), c.getPost().getImageUrl(), c.getClaimant().getId(),
                c.getClaimant().getFullName(), c.getClaimant().getStudentId(), c.getClaimant().getEmail(),
                c.getOwnershipDescription(), c.getStatus().name(), c.getCreatedAt()
        );
    }
}
