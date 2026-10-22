package com.mypictureapp.mypicture.dto.response;

import com.mypictureapp.mypicture.entity.FriendRequest;

public record IncomingFriendRequestResponse(
    String id,
    UserSummaryResponse form,   // người đã gửi lời mời
    String createdAt
) {
    public static IncomingFriendRequestResponse from(FriendRequest fr) {
        return new IncomingFriendRequestResponse(
            fr.getId(),
            UserSummaryResponse.from(fr.getFromUser(),FriendStatus.PENDING_RECEIVED),
            fr.getCreatedAt().toString()
        );
    }
}
