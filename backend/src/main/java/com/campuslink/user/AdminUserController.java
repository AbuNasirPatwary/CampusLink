package com.campuslink.user;

import com.campuslink.user.dto.UserResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {
    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> all() { return userService.listAllUsers(); }

    @GetMapping("/pending-alumni")
    public List<UserResponse> pendingAlumni() { return userService.pendingAlumni(); }

    @PatchMapping("/{id}/status")
    public UserResponse setStatus(@PathVariable UUID id, @RequestParam AccountStatus status) {
        return userService.setStatus(id, status);
    }
}
