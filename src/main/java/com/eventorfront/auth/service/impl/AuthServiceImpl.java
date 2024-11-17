package com.eventorfront.auth.service.impl;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.auth.client.AuthClient;
import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.auth.dto.response.LoginResponse;
import com.eventorfront.auth.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthClient authClient;

	@Override
	public ResponseEntity<LoginResponse> login(LoginRequest request) {
		return authClient.login(request);
	}

	@Override
	public void logout() {
		authClient.logout();
	}

	@Override
	public ResponseEntity<Void> signUp(SignUpRequest signUpRequest) {
		return authClient.signup(signUpRequest);
	}

	@Override
	public ResponseEntity<Void> sendEmailSignUp(String email) {
		return null;
	}

	@Override
	public boolean hasTokensInCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return false;
		}

		boolean hasAccessToken = false;
		boolean hasRefreshToken = false;

		for (Cookie cookie : cookies) {
			if ("Access-Token".equals(cookie.getName())) {
				hasAccessToken = true;
			}
			if ("Refresh-Token".equals(cookie.getName())) {
				hasRefreshToken = true;
			}
		}

		return hasAccessToken && hasRefreshToken;
	}

	@Override
	public void updateLastLoginAt(String accessToken, String refreshToken, LocalDateTime lastLoginAt) {

	}

	@Override
	public ResponseEntity<Void> checkEmailSignUp(String email, String certifyCode) {
		return null;
	}
}
