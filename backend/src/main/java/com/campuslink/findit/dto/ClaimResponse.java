package com.campuslink.findit.dto;

import com.campuslink.findit.ItemClaim;
import java.time.LocalDateTime;
import java.util.UUID;

public record ClaimResponse(
        UUID id,
        UUID postId,
        String postTitle,
        UUID claimantId,
        String claimantName,
        String claimantStudentId,
        String claimantEmail,
        String ownershipDescription,
        String status,
        LocalDateTime createdAt
) {
    public static ClaimResponse from(ItemClaim c) {
        return new ClaimResponse(
                c.getId(),
                c.getPost().getId(),
                c.getPost().getTitle(),
                c.getClaimant().getId(),
                c.getClaimant().getFullName(),
                c.getClaimant().getStudentId(),
                c.getClaimant().getEmail(),
                c.getOwnershipDescription(),
                c.getStatus().name(),
                c.getCreatedAt()
        );
    }
}
