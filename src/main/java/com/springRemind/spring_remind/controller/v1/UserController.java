package com.springRemind.spring_remind.controller.v1;

import com.springRemind.spring_remind.advice.exception.CUserNotFoundException;
import com.springRemind.spring_remind.entity.User;
import com.springRemind.spring_remind.model.response.CommonResult;
import com.springRemind.spring_remind.model.response.ListResult;
import com.springRemind.spring_remind.model.response.SingleResult;
import com.springRemind.spring_remind.repo.UserJpaRepo;
import com.springRemind.spring_remind.service.ResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "1. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {
    private final UserJpaRepo userJpaRepo;
    private final ResponseService responseService;

    @Operation(summary = "회원 조회", description = "모든 회원을 조회한다.")
    @GetMapping(value = "/user")
    public ListResult<User> findAllUser() {
        return responseService.getListResult(userJpaRepo.findAll());
    }

    @Operation(summary = "회원 단건 조회", description = "회원번호로 회원을 조회한다.")
    @GetMapping(value = "/user/{msrl}")
    public SingleResult<User> findUserByID(
            @Parameter(name = "msrl", description = "회원번호",required = true) @PathVariable("msrl") long msrl,
            @Parameter(name = "lang", description = "언어", required = true) @RequestParam(name = "lang", defaultValue = "ko") String lang
    ) {
        return responseService.getSingleResult(userJpaRepo.findById(msrl).orElseThrow(CUserNotFoundException::new));
    }

    @Operation(summary = "회원 입력", description = "회원을 입력한다.")
    @PostMapping(value = "/user")
    public SingleResult<User> save(
            @Parameter(name = "uid", description = "회원아이디",required = true) @RequestParam(value = "uid") String uid,
            @Parameter(name = "name", description = "회원이름",required = true) @RequestParam(value = "name")String name
    ) {
        User user = User.builder()
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Operation(summary = "회원 수정", description = "회원정보를 수정한다.")
    @PutMapping(value = "/user")
    public SingleResult<User> modify(
            @Parameter(name = "msrl", description = "회원번호",required = true) @RequestParam(value = "msrl") long msrl,
            @Parameter(name = "uid", description = "회원아이디",required = true) @RequestParam(value = "uid") String uid,
            @Parameter(name = "name", description = "회원이름",required = true) @RequestParam(value = "name")String name
    ) {
        User user = User.builder()
                .msrl(msrl)
                .uid(uid)
                .name(name)
                .build();
        return responseService.getSingleResult(userJpaRepo.save(user));
    }

    @Operation(summary = "회원 삭제", description = "회원번호로 회원정보를 삭제한다.")
    @PutMapping(value = "/user/{msrl}")
    public CommonResult delete(
            @Parameter(name = "msrl", description = "회원번호",required = true) @PathVariable("msrl") long msrl
    ) {
        userJpaRepo.deleteById(msrl);
        return responseService.getSuccessResult();
    }
}
