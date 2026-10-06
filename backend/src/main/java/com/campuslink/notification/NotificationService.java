package com.campuslink.notification;
import com.campuslink.common.exception.ApiException;
import com.campuslink.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class NotificationService{
 private final NotificationRepository repo; private final UserService users;
 public NotificationService(NotificationRepository r,UserService u){repo=r;users=u;}
 public List<NotificationResponse> mine(){return repo.findByUserOrderByCreatedAtDesc(users.getCurrentUser()).stream().map(NotificationResponse::from).toList();}
 @Transactional public NotificationResponse markRead(UUID id){Notification n=repo.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Notification not found."));if(!n.getUser().getId().equals(users.getCurrentUser().getId()))throw new ApiException(HttpStatus.FORBIDDEN,"Not your notification.");n.setReadFlag(true);return NotificationResponse.from(repo.save(n));}
 @Transactional public void markAllRead(){User u=users.getCurrentUser();for(Notification n:repo.findByUserOrderByCreatedAtDesc(u)){if(!n.isReadFlag()){n.setReadFlag(true);repo.save(n);}}}
 @Transactional public void create(User user,String message,String link){Notification n=new Notification();n.setUser(user);n.setMessage(message);n.setLink(link);repo.save(n);}
 public record NotificationResponse(UUID id,String message,String link,boolean read,java.time.LocalDateTime createdAt){public static NotificationResponse from(Notification n){return new NotificationResponse(n.getId(),n.getMessage(),n.getLink(),n.isReadFlag(),n.getCreatedAt());}}
}
