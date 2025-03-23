package com.eventorfront.auth.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	private static final String ACCESS_TOKEN = "access-token";
	private static final String REFRESH_TOKEN = "refresh-token";

	@GetMapping("/login")
	public String login(@RequestParam(required = false) String error, Model model) {
		model.addAttribute("error", error);
		return "auth/login";
	}

	@PostMapping("/login")
	public String login(@ModelAttribute LoginRequest request, HttpServletResponse response) {
		LoginResponse loginResponse = authService.login(request).getData();

		String accessToken = loginResponse.accessToken();
		String refreshToken = loginResponse.refreshToken();

		switch (loginResponse.userStatusName()) {
			case "탈퇴":
				return "redirect:/auth/login?error=" + URLEncoder.encode("탈퇴한 사용자입니다. 관리자에게 문의해 주세요.",
					StandardCharsets.UTF_8);
			case "휴면":
				return "redirect:/users/me/recover";
			default:
		}

		if (accessToken != null) {
			response.addCookie(CookieUtil.createCookie(ACCESS_TOKEN, accessToken));
		}

		if (refreshToken != null) {
			response.addCookie(CookieUtil.createCookie(REFRESH_TOKEN, refreshToken));
		}

		return "redirect:/";
	}

	/**
	 * 로그아웃 요청을 처리합니다.
	 */
	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {
		authService.logout();
		CookieUtil.revokeToken(response, ACCESS_TOKEN);
		CookieUtil.revokeToken(response, REFRESH_TOKEN);
		return "redirect:/";
	}

	/**
	 * 클라이언트의 쿠키에 저장된 액세스 토큰 및 리프레시 토큰이 존재하는지 확인합니다.
	 */
	@GetMapping("/hasTokens")
	public ResponseEntity<Boolean> hasTokensInCookie(HttpServletRequest request) {
		return ResponseEntity.ok().body(authService.hasTokensInCookie(request));
	}

	@GetMapping("/oauth2/authorization/{registrationId}")
	public String oauthAuthorization(@PathVariable String registrationId) {
		return authService.oauthAuthorization(registrationId).getData();
	}

	@GetMapping("/oauth2/login")
	public String oauthLogin(@RequestParam String accessToken, @RequestParam String refreshToken,
		HttpServletResponse response) {

		if (accessToken != null) {
			response.addCookie(CookieUtil.createCookie(ACCESS_TOKEN, accessToken));
		}

		if (refreshToken != null) {
			response.addCookie(CookieUtil.createCookie(REFRESH_TOKEN, refreshToken));
		}

		return "auth/oauth";
	}

}
