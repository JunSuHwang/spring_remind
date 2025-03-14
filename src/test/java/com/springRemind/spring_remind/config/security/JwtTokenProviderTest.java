package com.springRemind.spring_remind.config.security;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
class JwtTokenProviderTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    void getKey() {
    }

    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken("1", Collections.singletonList("ROLE_USER"));
        assertThat(jwtTokenProvider).isNotNull();
    }

    @Test
    void getAuthentication() {
    }

    @Test
    void getUserPk() {
        String token = jwtTokenProvider.createToken("1", Collections.singletonList("ROLE_UESR"));
        assertThat(jwtTokenProvider.getUserPk(token)).isEqualTo("1");
    }

    @Test
    void resolveToken() {
    }

    @Test
    void validateToken() {
        String token = jwtTokenProvider.createToken("1", Collections.singletonList("ROLE_UESR"));
        assertThat(jwtTokenProvider.validateToken(token)).isTrue();
    }
}