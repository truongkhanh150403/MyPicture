package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.FriendRequest;
import com.mypictureapp.mypicture.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FriendRequestRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private FriendRequestRopository friendRequestRopository;

    @Test
    void existsPendingBetween_shouldFindRegardlessOfDirection() {
        User from = em.persistFlushFind(User.builder()
                .username("from").password("pw").displayName("From").build());
        User to = em.persistFlushFind(User.builder()
                .username("to").password("pw").displayName("To").build());

        em.persistFlushFind(FriendRequest.builder().fromUser(from).toUser(to).build());

        // Check the original order
        assertThat(friendRequestRopository.existsPendingBetween(from, to)).isTrue();
        // Check the reverse order
        assertThat(friendRequestRopository.existsPendingBetween(to, from)).isTrue();
    }

    @Test
    void existsPendingBetween_shouldReturnFalseWhenNoRequestExists() {
        User u1 = em.persistFlushFind(User.builder()
                .username("stranger1").password("pw").displayName("Stranger1").build());
        User u2 = em.persistFlushFind(User.builder()
                .username("stranger2").password("pw").displayName("Stranger2").build());

        assertThat(friendRequestRopository.existsPendingBetween(u1, u2)).isFalse();
    }
}
