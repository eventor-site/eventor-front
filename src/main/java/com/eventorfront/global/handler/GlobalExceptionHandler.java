package com.eventorfront.global.handler;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.global.exception.GlobalException;
import com.eventorfront.global.exception.UnauthorizedException;
import com.eventorfront.global.exception.payload.ErrorStatus;

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

	@ExceptionHandler(value = GlobalException.class)
	public String globalHandleException(Model model, GlobalException exception) {

		ErrorStatus errorStatus = exception.getErrorStatus();
		HttpStatus status = errorStatus.getStatus();
		String message = errorStatus.getMessage();
		LocalDateTime timestamp = errorStatus.getTimestamp();

		model.addAttribute("message", message != null ? message : "An unexpected error occurred.");
		model.addAttribute("status", status != null ? status.value() : HttpStatus.INTERNAL_SERVER_ERROR.value());
		model.addAttribute("timestamp", timestamp != null ? timestamp.toString() : LocalDateTime.now().toString());

		return "common/error";
	}
}