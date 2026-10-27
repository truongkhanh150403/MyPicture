package com.mypictureapp.mypicture.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
    @NotBlank @Size(max = 100) String displayName,
    @NotBlank @Size(min = 3, max = 50) String username,
    @NotBlank @Size(min = 6, max = 72) String password,
    @NotBlank @Size(min = 6, max = 72) String confirmPassword,
    @Email @Size(max = 255) String email   // optional: don't have @NotBlank
) {}
