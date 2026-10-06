package com.campuslink.findit.dto;
import com.campuslink.findit.FindItType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
public record FindItPostRequest(
        @NotNull FindItType type,
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String location,
        @NotNull LocalDate eventDate,
        String imageUrl
) { }
