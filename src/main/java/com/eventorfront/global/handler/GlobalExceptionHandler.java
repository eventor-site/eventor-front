// package com.eventorfront.global.handler;
//
// import java.time.LocalDateTime;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.ControllerAdvice;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
// import com.eventorfront.global.exception.GlobalException;
// import com.eventorfront.global.exception.UnauthorizedException;
// import com.eventorfront.global.exception.payload.ErrorStatus;
//
// import feign.FeignException;
//
// @ControllerAdvice
// public class GlobalExceptionHandler {
//
// 	@ExceptionHandler({UnauthorizedException.class, FeignException.Unauthorized.class})
// 	public String handleUnauthorizedException(RedirectAttributes redirectAttributes) {
// 		// 리다이렉트 시 메시지 추가
// 		redirectAttributes.addFlashAttribute("message", "인증에 실패 했습니다. 다시 로그인해 주세요");
//
// 		// 로그인 페이지로 리다이렉트
// 		return "redirect:/auth/login";
// 	}
//
// 	@ExceptionHandler(FeignException.class)
// 	public String handleFeignException(Model model, FeignException e) {
// 		String message = e.getMessage(); // FeignException 메시지
// 		HttpStatus status = HttpStatus.valueOf(e.status()); // FeignException 에서 상태 코드 추출
// 		model.addAttribute("message", message);
// 		model.addAttribute("status", status.value());
// 		model.addAttribute("timestamp", LocalDateTime.now());
//
// 		return "common/error";
// 	}
//
// 	@ExceptionHandler(GlobalException.class)
// 	public String globalHandleException(Model model, GlobalException e) {
// 		ErrorStatus errorStatus = e.getErrorStatus();
// 		String message = errorStatus.getMessage();
// 		HttpStatus status = errorStatus.getStatus();
// 		LocalDateTime timestamp = errorStatus.getTimestamp();
//
// 		model.addAttribute("message", message);
// 		model.addAttribute("status", status);
// 		model.addAttribute("timestamp", timestamp);
//
// 		return "common/error";
// 	}
//
// }