package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.User;

public record UserProfileResponse(
        String id,
        String username,
        String displayName,
        String avatarUrl,
        String bio,
        FriendStatus friendStatus
) {
    public static UserProfileResponse from(User user, FriendStatus friendStatus) {
        return new UserProfileResponse(
                user.getId(),
                user.getUsername(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                user.getBio(),
                friendStatus
        );
    }
}
