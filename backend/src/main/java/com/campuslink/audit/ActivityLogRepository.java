package com.campuslink.audit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ActivityLogRepository extends JpaRepository<ActivityLog, UUID> {
    List<ActivityLog> findTop100ByOrderByCreatedAtDesc();
}
