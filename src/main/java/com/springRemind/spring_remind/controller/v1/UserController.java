package com.springRemind.spring_remind.controller.v1;

import com.springRemind.spring_remind.entity.User;
import com.springRemind.spring_remind.repo.UserJpaRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserController", description = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {
    private final UserJpaRepo userJpaRepo;

    @Operation(summary = "회원 조회", description = "모든 회원을 조회한다.")
    @GetMapping(value = "/user")
    public List<User> findAllUser() {
        return userJpaRepo.findAll();
    }

    @Operation(summary = "회원 입력", description = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public User save(
            @Parameter(name = "uid", description = "회원아이디",required = true) @RequestParam(value = "uid") String uid,
            @Parameter(name = "name", description = "회원이름",required = true) @RequestParam(value = "name")String name
    ) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return userJpaRepo.save(user);
    }
}
