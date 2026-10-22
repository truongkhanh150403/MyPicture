package com.mypictureapp.mypicture.dto.response;

public record AuthResponse(
        String accessToken,
        String refreshToken,
        UserResponse user
) {
}
