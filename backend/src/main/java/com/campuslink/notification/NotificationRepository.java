package com.campuslink.notification;
import com.campuslink.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface NotificationRepository extends JpaRepository<Notification,UUID>{List<Notification> findByUserOrderByCreatedAtDesc(User user);}
