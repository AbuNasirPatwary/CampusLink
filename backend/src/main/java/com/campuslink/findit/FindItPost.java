package com.campuslink.findit;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "findit_posts")
public class FindItPost extends BaseEntity {
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User owner;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private FindItType type;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 3000) private String description;
    @Column(nullable = false) private String location;
    @Column(nullable = false) private LocalDate eventDate;
    private String imageUrl;
    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private FindItStatus status = FindItStatus.OPEN;

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }
    public FindItType getType() { return type; }
    public void setType(FindItType type) { this.type = type; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public LocalDate getEventDate() { return eventDate; }
    public void setEventDate(LocalDate eventDate) { this.eventDate = eventDate; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public FindItStatus getStatus() { return status; }
    public void setStatus(FindItStatus status) { this.status = status; }
}
