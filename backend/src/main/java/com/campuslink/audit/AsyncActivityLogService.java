package com.campuslink.audit;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AsyncActivityLogService {
    private final ActivityLogRepository repository;
    public AsyncActivityLogService(ActivityLogRepository repository){this.repository=repository;}

    @Async("campusTaskExecutor")
    @Transactional
    public void record(UUID userId, String userEmail, String action, String details) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setUserEmail(userEmail);
        log.setAction(action);
        log.setDetails(details);
        log.setWorkerThread(Thread.currentThread().getName());
        repository.save(log);
    }
}
