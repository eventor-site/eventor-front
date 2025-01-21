package com.eventorfront.auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.response.LoginResponse;
import com.eventorfront.auth.service.AuthService;
import com.eventorfront.global.util.CookieUtil;

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

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest request, HttpServletResponse response) {
		LoginResponse loginResponse = authService.login(request).getBody();

		String accessToken;
		String refreshToken;

		if (Objects.isNull(loginResponse)) {
			return "redirect:/auth/login?error=" + URLEncoder.encode("로그인 실패", StandardCharsets.UTF_8);
		}

		accessToken = loginResponse.accessToken();
		refreshToken = loginResponse.refreshToken();

		if (accessToken != null) {
			response.addCookie(CookieUtil.createCookie("Access-Token", accessToken));
		}

		if (refreshToken != null) {
			response.addCookie(CookieUtil.createCookie("Refresh-Token", refreshToken));
		}

		return "redirect:/main";
	}

	/**
	 * 로그아웃 요청을 처리합니다.
	 */
	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		authService.logout();
		CookieUtil.revokeToken(response, "Access-Token");
		CookieUtil.revokeToken(response, "Refresh-Token");
		return "redirect:/auth/login";
	}

	/**
	 * 클라이언트의 쿠키에 저장된 액세스 토큰 및 리프레시 토큰이 존재하는지 확인합니다.
	 */
	@GetMapping("/has-tokens")
	public ResponseEntity<Boolean> hasTokensInCookie(HttpServletRequest request) {
		return ResponseEntity.status(HttpStatus.OK).body(authService.hasTokensInCookie(request));
	}

	@GetMapping("/oauth2/authorization/{registrationId}")
	public String oauthAuthorization(@PathVariable String registrationId) {
		return authService.naverAuthorization(registrationId).getBody();
	}

	@GetMapping("/oauth2/login")
	public String oauthSignup(@RequestParam String accessToken, @RequestParam String refreshToken,
		HttpServletResponse response) {

		if (accessToken != null) {
			response.addCookie(CookieUtil.createCookie("Access-Token", accessToken));
		}

		if (refreshToken != null) {
			response.addCookie(CookieUtil.createCookie("Refresh-Token", refreshToken));
		}

		return "redirect:/main";
	}
}
