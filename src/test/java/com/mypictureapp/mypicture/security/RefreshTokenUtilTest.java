package com.mypictureapp.mypicture.security;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RefreshTokenUtilTest {

    @Test
    void generate_shouldReturnUrlSafeStringOf43Chars() {
        String token = RefreshTokenUtil.generate();

        assertThat(token).hasSize(43).matches("[A-Za-z0-9_-]+");
    }

    @Test
    void generate_shouldReturnDifferentTokenEachTime() {
        assertThat(RefreshTokenUtil.generate()).isNotEqualTo(RefreshTokenUtil.generate());
    }

    @Test
    void hash_shouldMatchKnownSha256Value() {
        // giá trị chuẩn công khai của SHA-256("abc")
        assertThat(RefreshTokenUtil.hash("abc"))
                .isEqualTo("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad");
    }

    @Test
    void hash_shouldBeDeterministic() {
        String token = RefreshTokenUtil.generate();

        assertThat(RefreshTokenUtil.hash(token)).isEqualTo(RefreshTokenUtil.hash(token));
    }

    @Test
    void hash_shouldDifferForDifferentInputs() {
        assertThat(RefreshTokenUtil.hash("token-1")).isNotEqualTo(RefreshTokenUtil.hash("token-2"));
    }
}