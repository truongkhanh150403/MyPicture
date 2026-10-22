package com.mypictureapp.mypicture.repository;

import com.mypictureapp.mypicture.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired private TestEntityManager em;
    @Autowired private UserRepository userRepository;

    @Test
    void searchByDisplayName_shouldFindCaseInsensitive() {
        // Chuẩn bị: 1 user tên "Nguyen Van An"
        User user = em.persistFlushFind(User.builder()
                .username("an_nguyen")
                .password("hashed")
                .displayName("Nguyen Van An")
                .build());

        // Search bằng chữ thường, không dấu hoa — phải vẫn tìm thấy
        Page<User> result = userRepository.searchByDisplayName(
                "văn", "other_user_id", PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).extracting(User::getId).contains(user.getId());
    }

    @Test
    void searchByDisplayName_shouldExcludeUser() {
        User user = em.persistFlushFind(User.builder()
                .username("self_user")
                .password("hashed")
                .displayName("Self User")
                .build());

        // Truyền chính id của user này vào excludeUserId
        Page<User> result = userRepository.searchByDisplayName(
                "Self", user.getId(), PageRequest.of(0, 10)
        );

        // Phải KHÔNG thấy chính mình trong kết quả
        assertThat(result.getContent()).extracting(User::getId).doesNotContain(user.getId());
    }

}
