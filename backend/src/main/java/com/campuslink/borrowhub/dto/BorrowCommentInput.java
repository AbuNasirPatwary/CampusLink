package com.campuslink.borrowhub.dto;
import jakarta.validation.constraints.NotBlank;
public record BorrowCommentInput(@NotBlank String text) { }
