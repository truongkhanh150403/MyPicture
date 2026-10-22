package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.User;

public record UserSummaryResponse(
        String id,
        String displayName,
        String avatarUrl,
        FriendStatus friendStatus
) {
    public static UserSummaryResponse from(User user, FriendStatus friendStatus) {
        return new UserSummaryResponse(
                user.getId(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                friendStatus
        );
    }
}
