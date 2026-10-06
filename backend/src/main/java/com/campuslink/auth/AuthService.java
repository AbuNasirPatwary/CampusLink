package com.campuslink.auth;

import com.campuslink.audit.AsyncActivityLogService;
import com.campuslink.auth.dto.AuthResponse;
import com.campuslink.auth.dto.LoginRequest;
import com.campuslink.auth.dto.RegisterRequest;
import com.campuslink.common.exception.ApiException;
import com.campuslink.security.JwtService;
import com.campuslink.user.*;
import com.campuslink.user.dto.UserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AsyncActivityLogService activityLogs;

    @Value("${app.university-email-domain}")
    private String universityEmailDomain;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       AsyncActivityLogService activityLogs) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.activityLogs = activityLogs;
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (request.role() == Role.ADMIN) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Admin accounts cannot register publicly.");
        }
        String normalizedStudentId = request.studentId().trim();
        String normalizedEmail = request.email().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCase(normalizedEmail)) {
            throw new ApiException(HttpStatus.CONFLICT, "Email is already registered.");
        }
        if (userRepository.existsByStudentId(normalizedStudentId)) {
            throw new ApiException(HttpStatus.CONFLICT, "Student ID is already registered.");
        }
        if (request.role() == Role.STUDENT && !isUniversityEmail(normalizedEmail)) {
            throw new ApiException(HttpStatus.BAD_REQUEST,
                    "Students must register with a UIU university email under " + universityEmailDomain + ".");
        }
        User user = new User();
        user.setStudentId(normalizedStudentId);
        user.setFullName(request.fullName().trim());
        user.setEmail(normalizedEmail);
        user.setDepartment(clean(request.department()));
        user.setSemester(request.role() == Role.STUDENT ? clean(request.semester()) : null);
        user.setGraduationYear(request.role() == Role.ALUMNI ? request.graduationYear() : null);
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        user.setStatus(request.role() == Role.ALUMNI ? AccountStatus.PENDING : AccountStatus.ACTIVE);

        User saved = userRepository.save(user);
        activityLogs.record(saved.getId(), saved.getEmail(), "REGISTER", "Registered as " + saved.getRole().name());
        return UserResponse.from(saved);
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase();
        User user = userRepository.findByEmailIgnoreCase(normalizedEmail)
                .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password."));

        if (user.getStatus() == AccountStatus.PENDING) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Your alumni account is waiting for administrator approval.");
        }
        if (user.getStatus() == AccountStatus.REJECTED) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Your alumni registration was rejected. Please contact the CampusLink administrator.");
        }
        if (user.getStatus() == AccountStatus.SUSPENDED || user.getStatus() == AccountStatus.BLOCKED) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Your account is not currently active.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.password())
        );

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        activityLogs.record(user.getId(), user.getEmail(), "LOGIN", "Successful login");
        return new AuthResponse(token, UserResponse.from(user));
    }
    private boolean isUniversityEmail(String email) {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();
        String configuredDomain = universityEmailDomain == null ? "" : universityEmailDomain.trim().toLowerCase();
        if (configuredDomain.startsWith("@")) configuredDomain = configuredDomain.substring(1);
        int at = normalizedEmail.lastIndexOf('@');
        if (at < 0 || at == normalizedEmail.length() - 1 || configuredDomain.isBlank()) return false;
        String emailDomain = normalizedEmail.substring(at + 1);
        return emailDomain.equals(configuredDomain) || emailDomain.endsWith("." + configuredDomain);
    }

    private String clean(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

}
