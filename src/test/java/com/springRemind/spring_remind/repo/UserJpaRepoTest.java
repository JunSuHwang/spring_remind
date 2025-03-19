package com.springRemind.spring_remind.repo;

import com.springRemind.spring_remind.entity.User;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

// application.yml의 jpa의 ddl.auto 속성을 테이블을 생성하는 값으로 변경하면 에러 x
@DataJpaTest
class UserJpaRepoTest {

    @Autowired
    private UserJpaRepo userJpaRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void whenFindByUid_thenReturnUser() {
        String uid = "wnstn1234@gmail.com";
        String name = "wnstn";

        userJpaRepo.save(User.builder()
                .uid(uid)
                .password(passwordEncoder.encode("1234"))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );

        Optional<User> user = userJpaRepo.findByUid(uid);

        assertNotNull(user);
        assertTrue(user.isPresent());
        assertEquals(user.get().getName(), name);
        MatcherAssert.assertThat(user.get().getName(),is(name));
    }
}