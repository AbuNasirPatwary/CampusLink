package com.campuslink.auth.dto;

import com.campuslink.user.dto.UserResponse;

public record AuthResponse(String token, UserResponse user) { }
