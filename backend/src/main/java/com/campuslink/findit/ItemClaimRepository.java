package com.campuslink.findit;

import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ItemClaimRepository extends JpaRepository<ItemClaim, UUID> {
    List<ItemClaim> findAllByOrderByCreatedAtDesc();
    List<ItemClaim> findByClaimantOrderByCreatedAtDesc(User claimant);
    List<ItemClaim> findByPostAndStatus(FindItPost post, ClaimStatus status);
    boolean existsByPostAndClaimantAndStatus(FindItPost post, User claimant, ClaimStatus status);
}
