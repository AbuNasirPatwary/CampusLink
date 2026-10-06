package com.campuslink.user.dto;

import com.campuslink.user.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String studentId,
        String fullName,
        String email,
        String department,
        String semester,
        Integer graduationYear,
        String bio,
        String phone,
        String profileImageUrl,
        String role,
        String status,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(), user.getStudentId(), user.getFullName(), user.getEmail(),
                user.getDepartment(), user.getSemester(), user.getGraduationYear(),
                user.getBio(), user.getPhone(), user.getProfileImageUrl(),
                user.getRole().name(), user.getStatus().name(), user.getCreatedAt()
        );
    }
}
