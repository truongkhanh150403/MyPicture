package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.Friend;
import com.mypictureapp.mypicture.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private FriendRepository friendRepository;

    @Test
    void existsBetween_shouldFindRegardlessOfOrder() {
        User userX = em.persistFlushFind(User.builder()
                .username("userX").password("pw").displayName("User A").build());
        User userY = em.persistFlushFind(User.builder()
                .username("userY").password("pw").displayName("User B").build());

        em.persistFlushFind(Friend.of(userX, userY));

        assertThat(friendRepository.existsBetween(userX, userY)).isTrue();
        assertThat(friendRepository.existsBetween(userY, userX)).isTrue();
    }

    @Test
    void findAllByUser_shouldReturnAllFriendRegardlessOfSlot() {
        // id cố định để thứ tự chuẩn hóa userA/userB luôn xác định
        User user1 = em.persistFlushFind(User.builder()
                .id("00000000-0000-0000-0000-000000000001")
                .username("userA").password("pw").displayName("User 1").build());
        User user2 = em.persistFlushFind(User.builder()
                .id("00000000-0000-0000-0000-000000000002")
                .username("userB").password("pw").displayName("User 2").build());
        User user3 = em.persistFlushFind(User.builder()
                .id("00000000-0000-0000-0000-000000000003")
                .username("userC").password("pw").displayName("User 3").build());

        // Create friendships in both directions
        em.persistFlushFind(Friend.of(user1, user2)); // user2 nằm ở slot userB
        em.persistFlushFind(Friend.of(user2, user3)); // user2 nằm ở slot userA

        Page<Friend> result = friendRepository.findAllByUser(user2, PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    void findBetween_shouldFindRegardlessOfOrder() {
        User userX = em.persistFlushFind(User.builder()
                .username("findX").password("pw").displayName("Find X").build());
        User userY = em.persistFlushFind(User.builder()
                .username("findY").password("pw").displayName("Find Y").build());

        Friend saved = em.persistFlushFind(Friend.of(userX, userY));

        assertThat(friendRepository.findBetween(userX, userY)).contains(saved);
        assertThat(friendRepository.findBetween(userY, userX)).contains(saved);
    }

    @Test
    void findBetween_shouldReturnEmptyWhenNotFriends() {
        User userX = em.persistFlushFind(User.builder()
                .username("noFriendX").password("pw").displayName("No Friend X").build());
        User userY = em.persistFlushFind(User.builder()
                .username("noFriendY").password("pw").displayName("No Friend Y").build());

        assertThat(friendRepository.findBetween(userX, userY)).isEmpty();
    }
}
