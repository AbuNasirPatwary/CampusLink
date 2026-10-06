package com.campuslink.complaint.dto;
import com.campuslink.complaint.ComplaintPost;
import java.time.LocalDateTime;
import java.util.UUID;
public record ComplaintResponse(UUID id,UUID authorId,String authorName,String title,String content,String imageUrl,String videoUrl,
                                String status,String officialResponse,long reactionCount,LocalDateTime createdAt){
 public static ComplaintResponse from(ComplaintPost p,long reactions){return new ComplaintResponse(p.getId(),p.getAuthor().getId(),p.getAuthor().getFullName(),p.getTitle(),p.getContent(),p.getImageUrl(),p.getVideoUrl(),p.getStatus().name(),p.getOfficialResponse(),reactions,p.getCreatedAt());}
}
