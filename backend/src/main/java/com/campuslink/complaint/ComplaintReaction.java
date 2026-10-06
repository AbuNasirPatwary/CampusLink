package com.campuslink.complaint;

import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;

@Entity
@Table(name="complaint_reactions", uniqueConstraints=@UniqueConstraint(columnNames={"post_id","user_id"}))
public class ComplaintReaction extends BaseEntity {
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="post_id") private ComplaintPost post;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
    public ComplaintPost getPost(){return post;} public void setPost(ComplaintPost post){this.post=post;}
    public User getUser(){return user;} public void setUser(User user){this.user=user;}
}
