package com.mypictureapp.mypicture.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA cần no-arg constructor, nhưng không cho code ngoài gọi tùy tiện
@AllArgsConstructor(access = AccessLevel.PRIVATE)   // chỉ dùng nội bộ bởi of(), không public ra ngoài
@Builder(access = AccessLevel.PRIVATE)  // Builder cần constructor đủ field, nhưng vẫn giữ private
@Table(
        name = "friends",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_a_id", "user_b_id"})
)
public class Friend {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_a_id", nullable = false)
    private User userA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_b_id", nullable = false)
    private User userB;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public static Friend of(User userA, User userB) {
        if (userA.getId().compareTo(userB.getId()) < 0) {
            return Friend.builder().userA(userA).userB(userB).build();
        }
        return Friend.builder().userA(userB).userB(userA).build();
    }

    @PrePersist
    protected void onCreate() {
        if (this.id == null) {
            this.id = UUID.randomUUID().toString();
        }
        this.createdAt = LocalDateTime.now();
    }
}
