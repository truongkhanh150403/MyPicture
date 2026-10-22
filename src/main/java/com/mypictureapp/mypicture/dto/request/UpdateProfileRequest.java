package com.mypictureapp.mypicture.dto.request;

import jakarta.validation.constraints.Size;

// PATCH: field nào null nghĩa là "không đổi"
public record UpdateProfileRequest(
        @Size(min = 1, max = 100) String displayName,
        @Size(max = 500) String avatarUrl,
        @Size(max = 255) String avatarId,
        @Size(max = 500) String bio
) {}