package com.campuslink.talent.dto;
import com.campuslink.talent.TalentComment;
import java.time.LocalDateTime;
import java.util.UUID;
public record TalentCommentResponse(UUID id,UUID authorId,String authorName,String text,LocalDateTime createdAt){
 public static TalentCommentResponse from(TalentComment c){return new TalentCommentResponse(c.getId(),c.getAuthor().getId(),c.getAuthor().getFullName(),c.getText(),c.getCreatedAt());}
}
