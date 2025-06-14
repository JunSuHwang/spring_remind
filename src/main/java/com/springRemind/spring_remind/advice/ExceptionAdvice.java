package com.springRemind.spring_remind.advice;

import com.springRemind.spring_remind.advice.exception.*;
import com.springRemind.spring_remind.model.response.CommonResult;
import com.springRemind.spring_remind.service.ResponseService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
// 예외 발생 시 json형태로 반환
@RestControllerAdvice
public class ExceptionAdvice {

    private final ResponseService responseService;
    private final MessageSource messageSource;

    // 해당 예외가 발생하면 처리
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult defaultException(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("unknown.code")), getMessage("unknown.msg"));
    }

    @ExceptionHandler(CUserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult userNotFoundException(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
    }

    @ExceptionHandler(CEmailSigninFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected CommonResult emailSigninFailed(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("emailSigninFailed.code")), getMessage("emailSigninFailed.msg"));
    }

    @ExceptionHandler(CAuthenticationEntryPointException.class)
    public CommonResult authenticationEntryPointException(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("entryPointException.code")), getMessage("entryPointException.msg"));
    }

    @ExceptionHandler(AccessdeniedException.class)
    public CommonResult AccessDeniedException(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("accessdenied.code")), getMessage("accessdenied.msg"));
    }

    @ExceptionHandler(CComunicationException.class)
    public CommonResult communicationException(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("communicationError.code")), getMessage("communicationError.msg"));
    }

    @ExceptionHandler(CUserExistException.class)
    public CommonResult userexistException(HttpServletResponse request, Exception e) {
        return responseService.getFailResult(Integer.parseInt(getMessage("existingUser.code")), getMessage("existingUser.msg"));
    }

    private String getMessage(String code) {
        return getMessage(code, null);
    }

    private String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }
}
