package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // Find a user by their username
    // Dùng cho login: tìm user theo username để lấy password hash so sánh
    Optional<User> findByUsername(String username);

    // Check if a user exists by their username
    // Dùng cho register: check trùng username trước khi tạo mới
    boolean existsByUsername(String username);

    // Search for users by their display name
    // Dùng cho GET /users/search: tìm theo displayName (không phân biệt hoa/thường),
    // loại trừ chính người đang search, có phân trang.
    // Viết bằng @Query vì derived method tên sẽ quá dài (ContainingIgnoreCaseAndIdNot...)
    @Query("""
            SELECT u FROM User u
            WHERE LOWER(u.displayName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            AND u.id <> :excludeUserId
            """)
    Page<User> searchByDisplayName(
            @Param("keyword") String keyword,
            @Param("excludeUserId") String excludedUserId,
            Pageable pageable
    );

}
