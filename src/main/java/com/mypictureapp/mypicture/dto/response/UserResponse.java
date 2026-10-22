package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.User;

import java.time.LocalDateTime;

public record UserResponse(
        String id,
        String username,
        String displayName,
        String email,
        String avatarUrl,
        String bio,
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getEmail(),
                user.getAvatarUrl(),
                user.getBio(),
                user.getCreatedAt()
        );
    }
}
