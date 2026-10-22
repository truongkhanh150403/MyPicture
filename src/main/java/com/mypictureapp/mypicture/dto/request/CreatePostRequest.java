package com.mypictureapp.mypicture.dto.request;

import com.mypictureapp.mypicture.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePostRequest(
        @NotBlank @Size(max=500) @Pattern(regexp = "^https://.+") String imgageUrl,
        @NotBlank @Size(max = 255) String imageId
) {
}
