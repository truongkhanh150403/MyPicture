package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.FriendRequest;
import com.mypictureapp.mypicture.entity.FriendRequestStatus;

import java.time.LocalDateTime;

public record FriendRequestResponse(
        String id,
        String toUserId,
        FriendRequestStatus status,
        LocalDateTime createdAt
) {
    public static FriendRequestResponse from(FriendRequest fr) {
        return new FriendRequestResponse(
                fr.getId(),
                fr.getToUser().getId(),
                fr.getStatus(),
                fr.getCreatedAt()
        );
    }
}
