package com.mypictureapp.mypicture.security;

import com.mypictureapp.mypicture.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Instant;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final String CLAIM_USERNAME = "username";

    private final SecretKey secretKey;  // Secret key used for signing and verifying JWTs
    private final JwtProperties jwtProperties;

    public JwtTokenProvider (JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    /** Tạo access token: subject = userId, kèm username, hết hạn sau accessExpiration. */
    public String generateAccessToken(String userId, String username) {
        Instant now = Instant.now(); // Get the current time
        return Jwts.builder()
                .subject(userId)
                .claim(CLAIM_USERNAME, username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(jwtProperties.accessExpiration())))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact(); // Generate the JWT and return it as a string
    }

    /** true nếu token đúng chữ ký, đúng định dạng và chưa hết hạn. */
    public boolean validateToken(String token) {
        try {
            parseClaims(token); // Verify the token's signature and parse its claims
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.debug("Invalid JWT: {}", e.getMessage());
            return false;
        }
    }

    public String getUserId(String token) {
        return parseClaims(token).getSubject();
    }

    public String getUsername(String token) {
        return parseClaims(token).get(CLAIM_USERNAME, String.class);
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
