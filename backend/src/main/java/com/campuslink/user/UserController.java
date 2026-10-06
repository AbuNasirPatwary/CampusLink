package com.campuslink.user;

import com.campuslink.common.dto.MessageResponse;
import com.campuslink.user.dto.ChangePasswordRequest;
import com.campuslink.user.dto.UpdateProfileRequest;
import com.campuslink.user.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public UserResponse me() {
        return userService.me();
    }

    @PutMapping("/me")
    public UserResponse update(@Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }

    @PutMapping("/me/password")
    public MessageResponse changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        return new MessageResponse("Password changed successfully.");
    }
}
