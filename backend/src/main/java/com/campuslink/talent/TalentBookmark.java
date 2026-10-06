package com.campuslink.talent;
import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;
@Entity @Table(name="talent_bookmarks",uniqueConstraints=@UniqueConstraint(columnNames={"post_id","user_id"}))
public class TalentBookmark extends BaseEntity{
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="post_id") private TalentPost post;
 @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="user_id") private User user;
 public TalentPost getPost(){return post;} public void setPost(TalentPost post){this.post=post;}
 public User getUser(){return user;} public void setUser(User user){this.user=user;}
}
