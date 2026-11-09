package com.mypictureapp.mypicture.security;

import com.mypictureapp.mypicture.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private static final String SECRET = "test-secret-key-for-unit-tests-0123456789";

    private JwtTokenProvider provider;

    @BeforeEach
    void setUp() {
        provider = createProvider(SECRET, Duration.ofMinutes(15));
    }

    private JwtTokenProvider createProvider(String secret, Duration accessExpiration) {
        return new JwtTokenProvider(new JwtProperties(secret, accessExpiration, Duration.ofDays(7)));
    }

    @Test
    void generateAccessToken_shouldProduceTokenWithCorrectClaims() {
        String token = provider.generateAccessToken("user-1", "alice");

        assertThat(provider.validateToken(token)).isTrue();
        assertThat(provider.getUserId(token)).isEqualTo("user-1");
        assertThat(provider.getUsername(token)).isEqualTo("alice");
    }

    @Test
    void validateToken_shouldRejectExpiredToken() {
        // thời hạn âm: token đã hết hạn ngay lúc tạo
        JwtTokenProvider expiredProvider = createProvider(SECRET, Duration.ofSeconds(-1));
        String expiredToken = expiredProvider.generateAccessToken("user-1", "alice");

        assertThat(provider.validateToken(expiredToken)).isFalse();
    }

    @Test
    void validateToken_shouldRejectTokenSignedWithDifferentSecret() {
        JwtTokenProvider otherProvider =
                createProvider("another-secret-key-for-unit-tests-9876543210", Duration.ofMinutes(15));
        String foreignToken = otherProvider.generateAccessToken("user-1", "alice");

        assertThat(provider.validateToken(foreignToken)).isFalse();
    }

    @Test
    void validateToken_shouldRejectTokenWithSwappedPayload() {
        String tokenA = provider.generateAccessToken("user-1", "alice");
        String tokenB = provider.generateAccessToken("user-2", "bob");

        String[] a = tokenA.split("\\.");
        String[] b = tokenB.split("\\.");
        String forged = a[0] + "." + b[1] + "." + a[2];   // payload của B, chữ ký của A

        assertThat(provider.validateToken(forged)).isFalse();
    }

    @Test
    void validateToken_shouldRejectUnsignedToken() {
        String unsigned = Jwts.builder().subject("user-1").compact();

        assertThat(provider.validateToken(unsigned)).isFalse();
    }

    @Test
    void validateToken_shouldRejectGarbageNullAndEmpty() {
        assertThat(provider.validateToken("not-a-jwt")).isFalse();
        assertThat(provider.validateToken("")).isFalse();
        assertThat(provider.validateToken(null)).isFalse();
    }
}