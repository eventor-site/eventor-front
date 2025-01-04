package com.eventorfront.global.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.eventorfront.global.exception.UnauthorizedException;

import feign.FeignException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Void> handleUnauthorizedException() {
		return ResponseEntity.status(401).build();
	}

	@ExceptionHandler(FeignException.Unauthorized.class)
	public String handleNotFoundException() {
		// 로그인 페이지로 리다이렉트
		return "redirect:/auth/login";
	}
}