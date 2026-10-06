package com.campuslink.audit;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/activity-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminActivityLogController {
    private final ActivityLogRepository repository;
    public AdminActivityLogController(ActivityLogRepository repository){this.repository=repository;}

    @GetMapping
    public List<ActivityLogResponse> all(){
        return repository.findTop100ByOrderByCreatedAtDesc().stream().map(ActivityLogResponse::from).toList();
    }

    public record ActivityLogResponse(UUID id, UUID userId, String userEmail, String action,
                                      String details, String workerThread, LocalDateTime createdAt){
        static ActivityLogResponse from(ActivityLog log){
            return new ActivityLogResponse(log.getId(), log.getUserId(), log.getUserEmail(), log.getAction(),
                    log.getDetails(), log.getWorkerThread(), log.getCreatedAt());
        }
    }
}
