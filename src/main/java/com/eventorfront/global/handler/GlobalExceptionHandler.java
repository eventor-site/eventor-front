package com.eventorfront.global.handler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.eventorfront.global.exception.GlobalException;
import com.eventorfront.global.exception.UnauthorizedException;
import com.eventorfront.global.exception.payload.ErrorStatus;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({UnauthorizedException.class, FeignException.Unauthorized.class})
	public Object handleUnauthorizedException(HttpServletRequest request, Exception e) {

		// 💡 AJAX 요청인지 확인
		String ajaxHeader = request.getHeader("X-Ajax-Request");

		if (ajaxHeader != null) {
			// 🔹 AJAX 요청이면 JSON 응답 반환
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
		} else {//"인증에 실패 했습니다. 다시 로그인해 주세요"
			// 🔹 일반 요청이면 로그인 페이지로 리다이렉트
			return "redirect:/auth/login?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
		}
	}

	@ExceptionHandler(FeignException.class)
	public Object handleFeignException(HttpServletRequest request, Model model, FeignException e) {
		String ajaxHeader = request.getHeader("X-Ajax-Request");
		String errorMessage = e.getMessage(); // FeignException 메시지
		HttpStatus status = HttpStatus.valueOf(e.status()); // FeignException 에서 상태 코드 추출

		if (ajaxHeader != null) {
			return ResponseEntity.status(status).body(errorMessage);
		} else {
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("status", status.value());
			model.addAttribute("timestamp", LocalDateTime.now());

			return "common/error";
		}

	}

	@ExceptionHandler(GlobalException.class)
	public Object globalHandleException(HttpServletRequest request, Model model, GlobalException e) {
		String ajaxHeader = request.getHeader("X-Ajax-Request");

		ErrorStatus errorStatus = e.getErrorStatus();
		String errorMessage = errorStatus.getMessage();
		HttpStatus status = errorStatus.getStatus();
		LocalDateTime timestamp = errorStatus.getTimestamp();

		if (ajaxHeader != null) {
			return ResponseEntity.status(status).body(errorMessage);
		} else {
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("status", status.value());
			model.addAttribute("timestamp", timestamp);

			return "common/error";
		}
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public Object handleTypeMismatch(HttpServletRequest request, Model model) {
		String ajaxHeader = request.getHeader("X-Ajax-Request");

		String errorMessage = "잘못된 요청입니다.";
		HttpStatus status = HttpStatus.BAD_REQUEST;
		LocalDateTime timestamp = LocalDateTime.now();

		if (ajaxHeader != null) {
			return ResponseEntity.status(status).body(errorMessage);
		} else {
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("status", status.value());
			model.addAttribute("timestamp", timestamp);

			return "common/error";
		}
	}

}