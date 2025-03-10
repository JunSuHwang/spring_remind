package com.springRemind.spring_remind.controller.v1;

import com.springRemind.spring_remind.advice.exception.CEmailSigninFailedException;
import com.springRemind.spring_remind.config.security.JwtTokenProvider;
import com.springRemind.spring_remind.entity.User;
import com.springRemind.spring_remind.model.response.CommonResult;
import com.springRemind.spring_remind.model.response.SingleResult;
import com.springRemind.spring_remind.repo.UserJpaRepo;
import com.springRemind.spring_remind.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Tag(name = "SignController", description = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {
    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "로그인", description = "이메일 회원 로그인을 한다.")
    @GetMapping(value = "/signin")
    public SingleResult<String> signin(
            @Parameter(name = "id", description = "회원ID : 이메일",required = true) @RequestParam("id") String id,
            @Parameter(name = "password", description = "비밀번호",required = true) @RequestParam("password") String password
    ) {
        User user = userJpaRepo.findByUid(id).orElseThrow(CEmailSigninFailedException::new);
        if(!passwordEncoder.matches(password, user.getPassword())) throw new CEmailSigninFailedException();
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @Operation(summary = "가입", description = "회원가입을 한다.")
    @GetMapping(value = "/signup")
    public CommonResult signup(
            @Parameter(name = "id", description = "회원ID : 이메일",required = true) @RequestParam("id") String id,
            @Parameter(name = "password", description = "비밀번호",required = true) @RequestParam("password") String password,
            @Parameter(name = "name", description = "이름",required = true) @RequestParam("name") String name
    ) {
        userJpaRepo.save(User.builder()
                .uid(id)
                .password(passwordEncoder.encode(password))
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
        return responseService.getSuccessResult();
    }
}
