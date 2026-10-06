package com.campuslink.notification;
import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;
@Entity @Table(name="notifications")
public class Notification extends BaseEntity{
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User user;
 @Column(nullable=false,length=1000) private String message;
 private String link;
 @Column(nullable=false) private boolean readFlag;
 public User getUser(){return user;} public void setUser(User user){this.user=user;}
 public String getMessage(){return message;} public void setMessage(String message){this.message=message;}
 public String getLink(){return link;} public void setLink(String link){this.link=link;}
 public boolean isReadFlag(){return readFlag;} public void setReadFlag(boolean readFlag){this.readFlag=readFlag;}
}
