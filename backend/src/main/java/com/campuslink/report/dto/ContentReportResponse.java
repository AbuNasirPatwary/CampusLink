package com.campuslink.report.dto;
import com.campuslink.report.ContentReport;
import java.time.LocalDateTime;
import java.util.UUID;
public record ContentReportResponse(UUID id,String reporterName,String module,String targetType,UUID targetId,String reason,String status,LocalDateTime createdAt){
 public static ContentReportResponse from(ContentReport r){return new ContentReportResponse(r.getId(),r.getReporter().getFullName(),r.getModule().name(),r.getTargetType().name(),r.getTargetId(),r.getReason(),r.getStatus().name(),r.getCreatedAt());}
}
