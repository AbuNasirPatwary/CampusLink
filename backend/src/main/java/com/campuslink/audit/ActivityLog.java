package com.campuslink.audit;

import com.campuslink.common.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "activity_logs")
public class ActivityLog extends BaseEntity {
    private UUID userId;
    private String userEmail;
    @Column(nullable = false) private String action;
    @Column(length = 2000) private String details;
    private String workerThread;

    public UUID getUserId(){return userId;} public void setUserId(UUID userId){this.userId=userId;}
    public String getUserEmail(){return userEmail;} public void setUserEmail(String userEmail){this.userEmail=userEmail;}
    public String getAction(){return action;} public void setAction(String action){this.action=action;}
    public String getDetails(){return details;} public void setDetails(String details){this.details=details;}
    public String getWorkerThread(){return workerThread;} public void setWorkerThread(String workerThread){this.workerThread=workerThread;}
}
