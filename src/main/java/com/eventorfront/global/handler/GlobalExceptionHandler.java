package com.eventorfront.global.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.global.exception.UnauthorizedException;

import feign.FeignException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<Void> handleUnauthorizedException() {
		return ResponseEntity.status(401).build();
	}

	@ExceptionHandler(FeignException.Unauthorized.class)
	public String handleUnauthorizedException(RedirectAttributes redirectAttributes) {
		// 리다이렉트 시 메시지 추가
		redirectAttributes.addFlashAttribute("message", "인증에 실패 했습니다. 다시 로그인해 주세요");

		// 로그인 페이지로 리다이렉트
		return "redirect:/auth/login";
	}
}