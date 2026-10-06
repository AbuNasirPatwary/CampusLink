package com.campuslink.complaint.dto;
import jakarta.validation.constraints.NotBlank;
public record ComplaintInput(@NotBlank String title,@NotBlank String content,String imageUrl,String videoUrl){}
