package com.mypictureapp.mypicture.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public final class RefreshTokenUtil {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RefreshTokenUtil() {}

    /** Sinh refresh token mới: 32 byte ngẫu nhiên (256 bit), mã hóa Base64 URL-safe (43 ký tự). */
    public static String generate() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Hash SHA-256 dạng hex (64 ký tự) để lưu vào cột token_hash. Cùng đầu vào luôn ra cùng kết quả. */
    public static String hash(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashed = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }
}
