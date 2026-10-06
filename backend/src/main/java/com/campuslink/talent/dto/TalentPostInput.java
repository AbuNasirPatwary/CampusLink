package com.campuslink.talent.dto;
import com.campuslink.talent.TalentCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public record TalentPostInput(@NotNull TalentCategory category,@NotBlank String title,String content,String mediaUrl,String thumbnailUrl,String location){}
