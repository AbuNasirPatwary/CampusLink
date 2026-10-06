package com.campuslink.borrowhub.dto;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
public record BorrowRequestInput(@NotBlank String title, @NotBlank String description, String imageUrl,
                                 LocalDate requiredDate, LocalDate expectedReturnDate) { }
