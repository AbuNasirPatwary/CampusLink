package com.campuslink.complaint.dto;
import com.campuslink.complaint.ComplaintComment;
import java.time.LocalDateTime;
import java.util.UUID;
public record ComplaintCommentResponse(UUID id,UUID authorId,String authorName,String text,LocalDateTime createdAt){
 public static ComplaintCommentResponse from(ComplaintComment c){return new ComplaintCommentResponse(c.getId(),c.getAuthor().getId(),c.getAuthor().getFullName(),c.getText(),c.getCreatedAt());}
}
