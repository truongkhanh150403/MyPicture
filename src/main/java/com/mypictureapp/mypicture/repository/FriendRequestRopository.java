package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.FriendRequest;
import com.mypictureapp.mypicture.entity.FriendRequestStatus;
import com.mypictureapp.mypicture.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRopository extends JpaRepository<FriendRequest, String> {

    /**
     * Dùng cho POST /friends/requests: kiểm tra giữa userX và userY
     * đã có lời mời đang PENDING chưa (bất kể ai gửi trước) — tránh tạo trùng.
     */

    @Query("""
            SELECT (COUNT(fr) > 0) FROM FriendRequest fr
            WHERE fr.status = 'PENDING'
            AND ((fr.fromUser = :userX AND fr.toUser = :userY)
              OR (fr.fromUser = :userY AND fr.toUser = :userX))
            """)
    boolean existsPendingBetween(@Param("userX") User userX, @Param("userY") User userY);

    /**
     * Dùng cho GET /friends/requests/incoming: danh sách lời mời user hiện tại NHẬN được,
     * đang ở trạng thái PENDING, mới nhất lên đầu, có phân trang.
     */
    Page<FriendRequest> findByToUserAndStatusOrderByCreatedAtDesc(
            User toUser,
            FriendRequestStatus status,
            Pageable pageable
    );

}
