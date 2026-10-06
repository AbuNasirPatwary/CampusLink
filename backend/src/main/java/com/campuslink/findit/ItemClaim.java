package com.campuslink.findit;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "findit_claims")
public class ItemClaim extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private FindItPost post;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User claimant;
    @Column(nullable = false, length = 2000)
    private String ownershipDescription;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ClaimStatus status = ClaimStatus.PENDING;

    public FindItPost getPost() { return post; }
    public void setPost(FindItPost post) { this.post = post; }
    public User getClaimant() { return claimant; }
    public void setClaimant(User claimant) { this.claimant = claimant; }
    public String getOwnershipDescription() { return ownershipDescription; }
    public void setOwnershipDescription(String ownershipDescription) { this.ownershipDescription = ownershipDescription; }
    public ClaimStatus getStatus() { return status; }
    public void setStatus(ClaimStatus status) { this.status = status; }
}
