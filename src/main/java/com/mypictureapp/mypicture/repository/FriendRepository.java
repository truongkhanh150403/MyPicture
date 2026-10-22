package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.Friend;
import com.mypictureapp.mypicture.entity.FriendRequest;
import com.mypictureapp.mypicture.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface FriendRepository extends JpaRepository<Friend, String> {

    /**
     * Dùng cho POST /friends/requests: kiểm tra 2 user đã là bạn chưa
     * trước khi cho phép gửi lời mời kết bạn mới.
     */
    @Query("""
            SELECT COUNT(f) > 0 FROM Friend f
            WHERE (f.userA = :userX AND f.userB = :userY)
            OR (f.userA =:userY AND f.userB = :userX)
            """)
    boolean existsBetween(@Param("userX") User userX, @Param("userY") User userY);

    /**
     * Dùng cho DELETE /friends/{userId}: tìm đúng bản ghi Friend giữa
     * user hiện tại và user muốn hủy kết bạn, để xóa.
     */
    @Query("""
            SELECT f FROM Friend f
            WHERE (f.userA = :userX AND f.userB = :userY)
            OR (f.userA =:userY AND f.userB = :userX)
            """)
    Optional<Friend> findBetween(@Param("userX") User userX, @Param("userY") User userY);

    /**
     * Dùng cho GET /friends: danh sách bạn bè của 1 user — user đó có thể
     * nằm ở userA hoặc userB tùy từng dòng, nên phải OR cả 2 phía.
     */
    @Query("""
            SELECT f FROM Friend f
            WHERE f.userA = :user OR f.userB = :user
            ORDER BY f.createdAt DESC
            """)
    Page<Friend> findAllByUser(@Param("user") User user, Pageable pageable);

}
