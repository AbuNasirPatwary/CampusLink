package com.campuslink.talent.dto;
import jakarta.validation.constraints.NotBlank;
public record TalentCommentInput(@NotBlank String text){}
