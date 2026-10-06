package com.campuslink.borrowhub;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "borrow_comments")
public class BorrowComment extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private BorrowRequest request;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User author;
    @Column(nullable = false, length = 1500)
    private String text;
    private boolean removed;

    public BorrowRequest getRequest() { return request; }
    public void setRequest(BorrowRequest request) { this.request = request; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean isRemoved() { return removed; }
    public void setRemoved(boolean removed) { this.removed = removed; }
}
