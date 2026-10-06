package com.campuslink.notification;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/notifications")
public class NotificationController{
 private final NotificationService service; public NotificationController(NotificationService s){service=s;}
 @GetMapping public List<NotificationService.NotificationResponse> mine(){return service.mine();}
 @PatchMapping("/{id}/read") public NotificationService.NotificationResponse read(@PathVariable UUID id){return service.markRead(id);}
 @PatchMapping("/read-all") public Map<String,String> readAll(){service.markAllRead();return Map.of("message","All notifications marked as read.");}
}
