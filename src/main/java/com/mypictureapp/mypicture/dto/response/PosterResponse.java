package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.User;

public record PosterResponse(
        String id,
        String displayName,
        String avatarUrl
) {
    public static PosterResponse from(User user) {
        return new PosterResponse(user.getId(), user.getDisplayName(), user.getAvatarUrl());
    }
}
