package com.eventorfront.auth.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.auth.client.AuthClient;
import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.request.OauthSignUpRequest;
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
	public ResponseEntity<String> naverAuthorization(String registrationId) {
		return authClient.naverAuthorization(registrationId);
	}

	public ResponseEntity<LoginResponse> oauthSignup(OauthSignUpRequest oauthSignUpRequest) {
		return authClient.oauthSignup(oauthSignUpRequest);
	}

}
