package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.Post;

import java.time.LocalDateTime;

public record PostResponse(
        String id,
        PosterResponse poster,
        String imageUrl,
        String imageId,
        LocalDateTime createdAt
) {
    public static PostResponse from(Post post) {
        return new PostResponse(
                post.getId(),
                PosterResponse.from(post.getPoster()),
                post.getImageUrl(),
                post.getImageId(),
                post.getCreatedAt()
        );
    }
}