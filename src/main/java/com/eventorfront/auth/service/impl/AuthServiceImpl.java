package com.eventorfront.auth.service.impl;

import org.springframework.stereotype.Service;

import com.eventorfront.auth.client.AuthClient;
import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.response.LoginResponse;
import com.eventorfront.auth.service.AuthService;
import com.eventorfront.global.dto.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	private final AuthClient authClient;

	@Override
	public ApiResponse<LoginResponse> login(LoginRequest request) {
		return authClient.login(request).getBody();
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
			if ("access-token".equals(cookie.getName())) {
				hasAccessToken = true;
			}
			if ("refresh-token".equals(cookie.getName())) {
				hasRefreshToken = true;
			}
		}

		return hasAccessToken && hasRefreshToken;
	}

	@Override
	public ApiResponse<String> oauthAuthorization(String registrationId) {
		return authClient.oauthAuthorization(registrationId).getBody();
	}

}
