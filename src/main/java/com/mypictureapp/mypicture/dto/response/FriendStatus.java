package com.mypictureapp.mypicture.dto.response;

// Không lưu DB, được Service tính ra từ bảng friends và friend_requests
public enum FriendStatus {
    NONE,              // chưa có quan hệ gì
    PENDING_SENT,      // mình đã gửi lời mời, chờ họ trả lời
    PENDING_RECEIVED,  // họ đã gửi lời mời cho mình
    FRIEND             // đã là bạn bè
}