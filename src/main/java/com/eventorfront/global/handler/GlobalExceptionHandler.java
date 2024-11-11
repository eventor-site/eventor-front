package com.eventorfront.global.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import feign.FeignException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(FeignException.Unauthorized.class)
	public String handleUnauthorizedException() {
		// 로그인 페이지로 리다이렉트
		return "redirect:/auth/login";
	}
}