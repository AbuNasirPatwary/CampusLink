package com.campuslink.report;
import com.campuslink.common.model.BaseEntity;
import com.campuslink.user.User;
import jakarta.persistence.*;
import java.util.UUID;
@Entity @Table(name="content_reports")
public class ContentReport extends BaseEntity{
 @ManyToOne(optional=false,fetch=FetchType.LAZY) private User reporter;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private ReportModule module;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private ReportTargetType targetType;
 @Column(nullable=false) private UUID targetId;
 @Column(nullable=false,length=1500) private String reason;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private ReportStatus status=ReportStatus.OPEN;
 public User getReporter(){return reporter;} public void setReporter(User reporter){this.reporter=reporter;}
 public ReportModule getModule(){return module;} public void setModule(ReportModule module){this.module=module;}
 public ReportTargetType getTargetType(){return targetType;} public void setTargetType(ReportTargetType targetType){this.targetType=targetType;}
 public UUID getTargetId(){return targetId;} public void setTargetId(UUID targetId){this.targetId=targetId;}
 public String getReason(){return reason;} public void setReason(String reason){this.reason=reason;}
 public ReportStatus getStatus(){return status;} public void setStatus(ReportStatus status){this.status=status;}
}
