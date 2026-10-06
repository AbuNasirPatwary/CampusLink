package com.campuslink.talent.dto;
import com.campuslink.talent.TalentPost;
import java.time.LocalDateTime;
import java.util.UUID;
public record TalentPostResponse(UUID id,UUID authorId,String authorName,String authorRole,String authorProfileImageUrl,String category,String title,String content,
                                 String mediaUrl,String thumbnailUrl,String location,String status,boolean featured,long likeCount,LocalDateTime createdAt){
 public static TalentPostResponse from(TalentPost p,long likes){return new TalentPostResponse(p.getId(),p.getAuthor().getId(),p.getAuthor().getFullName(),p.getAuthor().getRole().name(),p.getAuthor().getProfileImageUrl(),p.getCategory().name(),p.getTitle(),p.getContent(),p.getMediaUrl(),p.getThumbnailUrl(),p.getLocation(),p.getStatus().name(),p.isFeatured(),likes,p.getCreatedAt());}
}
