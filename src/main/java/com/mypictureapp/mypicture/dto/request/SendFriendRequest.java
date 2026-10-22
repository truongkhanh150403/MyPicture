package com.mypictureapp.mypicture.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SendFriendRequest(
    @NotBlank String toUserId
) {
}
