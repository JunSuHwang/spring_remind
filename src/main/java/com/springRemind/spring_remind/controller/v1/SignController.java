package com.springRemind.spring_remind.controller.v1;

import com.springRemind.spring_remind.advice.exception.CEmailSigninFailedException;
import com.springRemind.spring_remind.advice.exception.CUserExistException;
import com.springRemind.spring_remind.advice.exception.CUserNotFoundException;
import com.springRemind.spring_remind.config.security.JwtTokenProvider;
import com.springRemind.spring_remind.entity.User;
import com.springRemind.spring_remind.model.response.CommonResult;
import com.springRemind.spring_remind.model.response.SingleResult;
import com.springRemind.spring_remind.model.social.KakaoProfile;
import com.springRemind.spring_remind.repo.UserJpaRepo;
import com.springRemind.spring_remind.service.ResponseService;
import com.springRemind.spring_remind.service.user.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@Tag(name = "SignController", description = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class SignController {
    private final UserJpaRepo userJpaRepo;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResponseService responseService;
    private final KakaoService kakaoService;
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
    @PostMapping(value = "/signup")
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

    @Operation(summary = "소셜 로그인", description = "소셜 회원 로그인을 한다.")
    @PostMapping(value = "/signin/social")
    public SingleResult<String> signinByProvider(
            @Parameter(name = "provider", description = "서비스 제공자 provider", required = true) @RequestParam(value = "provider", defaultValue = "kakao") String provider,
            @Parameter(name = "accessToken", description = "소셜 access_token", required = true) @RequestParam("accessToken") String accessToken
    ) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        User user = userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider).orElseThrow(CUserNotFoundException::new);
        return responseService.getSingleResult(jwtTokenProvider.createToken(String.valueOf(user.getMsrl()), user.getRoles()));
    }

    @Operation(summary = "소셜 계정 가입", description = "소셜 계정 회원가입을 한다.")
    @PostMapping(value = "/signup/social")
    public CommonResult signupProvider(
            @Parameter(name = "provider", description = "서비스 제공자 provider",required = true) @RequestParam(value = "provider", defaultValue = "kakao") String provider,
            @Parameter(name = "accessToken", description = "소셜 accessToken",required = true) @RequestParam("accessToken") String accessToken,
            @Parameter(name = "name", description = "이름",required = true) @RequestParam("name") String name
    ) {
        KakaoProfile profile = kakaoService.getKakaoProfile(accessToken);
        Optional<User> user = userJpaRepo.findByUidAndProvider(String.valueOf(profile.getId()), provider);
        if (user.isPresent())
            throw new CUserExistException();
        userJpaRepo.save(User.builder()
                .uid(String.valueOf(profile.getId()))
                .provider(provider)
                .name(name)
                .roles(Collections.singletonList("ROLE_USER"))
                .build()
        );
        return responseService.getSuccessResult();
    }
}