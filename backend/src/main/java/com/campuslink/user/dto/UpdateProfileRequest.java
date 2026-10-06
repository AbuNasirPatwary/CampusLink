package com.campuslink.user.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileRequest(
        @NotBlank String fullName,
        String department,
        String semester,
        Integer graduationYear,
        String bio,
        String phone,
        String profileImageUrl
) { }
