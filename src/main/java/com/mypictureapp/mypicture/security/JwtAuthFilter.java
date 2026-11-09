package com.mypictureapp.mypicture.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Không gắn @Component: sẽ được tạo thủ công trong SecurityConfig (bước 7)
@RequiredArgsConstructor //annotation tự sinh constructor nhận tất cả field final
public class JwtAuthFilter extends OncePerRequestFilter { // lớp cha có sẵn của Spring để tạo filter

    private static final String BEARER_PREFIX = "Bearer ";  // Chuỗi tiền tố trong header Authorization, cách nhau 1 dấu cách

    private final JwtTokenProvider jwtTokenProvider;

    /** Lọc request, nếu có token hợp lệ thì tạo Authentication và lưu vào SecurityContext. */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request); // lấy token từ header Authorization

        if (token != null && jwtTokenProvider.validateToken(token)) {
            AuthUser authUser = new AuthUser(
                    jwtTokenProvider.getUserId(token),
                    jwtTokenProvider.getUsername(token)
            );

            UsernamePasswordAuthenticationToken authentication =
                    UsernamePasswordAuthenticationToken.authenticated(authUser, null, List.of());

            // Thiết lập chi tiết xác thực từ request (ví dụ: địa chỉ IP, thông tin trình duyệt)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);   // luôn cho request đi tiếp
    }

    /** Trích xuất token từ header Authorization của request. */
    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);   //hằng số chuỗi "Authorization" do Spring cung cấp.
        if (header != null && header.startsWith(BEARER_PREFIX)) {
            return header.substring(BEARER_PREFIX.length());
        }
        return null;
    }
}