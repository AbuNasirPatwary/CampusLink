package com.campuslink.borrowhub;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "borrow_requests")
public class BorrowRequest extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 3000) private String description;
    private String imageUrl;
    private LocalDate requiredDate;
    private LocalDate expectedReturnDate;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private BorrowStatus status = BorrowStatus.OPEN;

    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public LocalDate getRequiredDate() { return requiredDate; }
    public void setRequiredDate(LocalDate requiredDate) { this.requiredDate = requiredDate; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public void setExpectedReturnDate(LocalDate expectedReturnDate) { this.expectedReturnDate = expectedReturnDate; }
    public BorrowStatus getStatus() { return status; }
    public void setStatus(BorrowStatus status) { this.status = status; }
}
