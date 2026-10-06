package com.campuslink.auth.dto;

import com.campuslink.user.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Phase-One registration. The only university identity fields required for
 * both account types are Student ID and email. Student accounts additionally
 * require the configured UIU email domain; alumni may use any valid email and
 * are held for administrator approval.
 */
public record RegisterRequest(
        @NotNull Role role,
        @NotBlank String studentId,
        @NotBlank String fullName,
        @Email @NotBlank String email,
        String department,
        String semester,
        Integer graduationYear,
        @NotBlank @Size(min = 8) String password
) { }
