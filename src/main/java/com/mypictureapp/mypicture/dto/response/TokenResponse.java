package com.mypictureapp.mypicture.dto.response;

public record TokenResponse(
        String accessToken,
        String refreshToken
) {
}
