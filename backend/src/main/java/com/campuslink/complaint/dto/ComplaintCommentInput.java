package com.campuslink.complaint.dto;
import jakarta.validation.constraints.NotBlank;
public record ComplaintCommentInput(@NotBlank String text){}
