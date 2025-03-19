package com.springRemind.spring_remind.controller.v1;

import com.springRemind.spring_remind.entity.User;
import com.springRemind.spring_remind.repo.UserJpaRepo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SignControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserJpaRepo userJpaRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() throws Exception {
        userJpaRepo.save(User.builder()
                .uid("wnstntest@gmail.com")
                .name("wnstntest")
                .password(passwordEncoder.encode("1234"))
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
    }

    @Test
    void signin() throws Exception{
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "wnstntest@gmail.com");
        params.add("password", "1234");
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/signin").params(params))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").exists());
    }

    @Test
    void signup() throws Exception{
        long epochTime = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "wnstntest_" + epochTime + "@gmail.com");
        params.add("password", "12345");
        params.add("name", "wnstntest_" + epochTime);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/signup").params(params))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").exists());
    }
}