package com.springRemind.spring_remind.controller.exception;

import com.springRemind.spring_remind.advice.exception.AccessdeniedException;
import com.springRemind.spring_remind.advice.exception.CAuthenticationEntryPointException;
import com.springRemind.spring_remind.model.response.CommonResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {

    @GetMapping(value = "/entrypoint")
    public CommonResult entrypointException() {
        throw new CAuthenticationEntryPointException();
    }

    @GetMapping(value = "/accessdenied")
    public CommonResult accessdeniedException() {
        throw new AccessdeniedException();
    }
}
