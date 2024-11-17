package com.eventorfront.auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.auth.dto.response.LoginResponse;
import com.eventorfront.auth.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증 관련 요청을 처리하는 컨트롤러입니다.
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
	private final AuthService authService;

	@GetMapping("/login")
	public String login() {
		return "auth/login";
	}

	@GetMapping("/sign-up")
	public String signUp() {
		return "auth/sign-up";
	}

	@PostMapping("/sign-up")
	public String signUp(@ModelAttribute SignUpRequest signUpRequest) {
		authService.signUp(signUpRequest);
		return "redirect:/auth/login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest request, HttpServletResponse response) {
		LoginResponse loginResponse = authService.login(request).getBody();

		String accessToken;
		String refreshToken;
		LocalDateTime lastLoginAt;

		if (Objects.isNull(loginResponse)) {
			return "redirect:/auth/login?error=" + URLEncoder.encode("로그인 실패", StandardCharsets.UTF_8);
		}

		accessToken = loginResponse.accessToken();
		refreshToken = loginResponse.refreshToken();
		lastLoginAt = loginResponse.lastLoginAt();

		if (accessToken != null) {
			response.addCookie(createCookie("Access-Token", accessToken));
		}

		if (refreshToken != null) {
			response.addCookie(createCookie("Refresh-Token", refreshToken));
		}

		authService.updateLastLoginAt(accessToken, refreshToken, lastLoginAt);

		return "redirect:/start";
	}

	/**
	 * 로그아웃 요청을 처리합니다.
	 */
	@PostMapping("/logout")
	public ResponseEntity<Void> logout(HttpServletResponse response) {
		authService.logout();
		revokeToken(response, "Access-Token");
		revokeToken(response, "Refresh-Token");
		return ResponseEntity.ok().build();
	}

	/**
	 * 클라이언트의 쿠키에 저장된 액세스 토큰 및 리프레시 토큰이 존재하는지 확인합니다.
	 */
	@GetMapping("/has-tokens")
	public ResponseEntity<Boolean> hasTokensInCookie(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.hasTokensInCookie(request));
	}

	/**
	 * 주어진 키와 값을 사용하여 쿠키를 생성합니다.
	 */
	private Cookie createCookie(String key, String value) {
		Cookie cookie = new Cookie(key, URLEncoder.encode(value, StandardCharsets.UTF_8));
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		return cookie;
	}

	/**
	 * 주어진 쿠키 이름을 사용하여 토큰을 무효화합니다.
	 */
	private void revokeToken(HttpServletResponse response, String cookieName) {
		Cookie revokedTokenCookie = new Cookie(cookieName, "");
		revokedTokenCookie.setHttpOnly(true);
		revokedTokenCookie.setMaxAge(0);
		revokedTokenCookie.setPath("/");
		response.addCookie(revokedTokenCookie);
	}
}
