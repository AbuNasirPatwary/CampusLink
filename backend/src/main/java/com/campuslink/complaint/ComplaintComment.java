package com.campuslink.complaint;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="complaint_comments")
public class ComplaintComment extends BaseEntity {
    @ManyToOne(optional=false,fetch=FetchType.LAZY) private ComplaintPost post;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) private User author;
    @Column(nullable=false,length=1500) private String text;
    private boolean removed;
    public ComplaintPost getPost(){return post;} public void setPost(ComplaintPost post){this.post=post;}
    public User getAuthor(){return author;} public void setAuthor(User author){this.author=author;}
    public String getText(){return text;} public void setText(String text){this.text=text;}
    public boolean isRemoved(){return removed;} public void setRemoved(boolean removed){this.removed=removed;}
}
