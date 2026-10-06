package com.campuslink.user;

import com.campuslink.audit.AsyncActivityLogService;
import com.campuslink.common.exception.ApiException;
import com.campuslink.user.dto.ChangePasswordRequest;
import com.campuslink.user.dto.UpdateProfileRequest;
import com.campuslink.user.dto.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AsyncActivityLogService activityLogs;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AsyncActivityLogService activityLogs) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.activityLogs = activityLogs;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException(HttpStatus.UNAUTHORIZED, "Login required.");
        }
        return userRepository.findByEmailIgnoreCase(authentication.getName())
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "User not found."));
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found."));
    }

    public UserResponse me() {
        return UserResponse.from(getCurrentUser());
    }

    @Transactional
    public UserResponse updateProfile(UpdateProfileRequest request) {
        User user = getCurrentUser();
        user.setFullName(request.fullName());
        user.setDepartment(request.department());
        if (user.getRole() == Role.STUDENT) {
            user.setSemester(request.semester());
        } else if (user.getRole() == Role.ALUMNI) {
            user.setGraduationYear(request.graduationYear());
        }
        user.setBio(request.bio());
        user.setPhone(request.phone());
        user.setProfileImageUrl(request.profileImageUrl());
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        User user = getCurrentUser();
        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Current password is incorrect.");
        }
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }

    public List<UserResponse> listAllUsers() {
        return userRepository.findAllByOrderByCreatedAtDesc().stream().map(UserResponse::from).toList();
    }

    public List<UserResponse> pendingAlumni() {
        return userRepository.findByRoleAndStatusOrderByCreatedAtDesc(Role.ALUMNI, AccountStatus.PENDING)
                .stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse setStatus(UUID id, AccountStatus status) {
        User user = getById(id);
        if (user.getRole() == Role.ADMIN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Admin status cannot be changed here.");
        }
        user.setStatus(status);
        User saved = userRepository.save(user);
        User admin = getCurrentUser();
        activityLogs.record(admin.getId(), admin.getEmail(), "USER_STATUS_CHANGE",
                saved.getRole() + " " + saved.getEmail() + " -> " + status);
        return UserResponse.from(saved);
    }
}
