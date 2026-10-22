package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.Post;

import java.time.LocalDateTime;

public record PostSummaryResponse(
        String id,
        String imageUrl,
        LocalDateTime createdAt
) {
    public static PostSummaryResponse from(Post post) {
        return new PostSummaryResponse(post.getId(), post.getImageUrl(), post.getCreatedAt());
    }
}