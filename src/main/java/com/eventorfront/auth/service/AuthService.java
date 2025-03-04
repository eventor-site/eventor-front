package com.eventorfront.auth.service;

import org.springframework.http.ResponseEntity;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.response.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 인증과 관련된 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface AuthService {
	ResponseEntity<LoginResponse> login(LoginRequest loginRequest);

	/**
	 * 사용자의 로그아웃 요청을 처리합니다.
	 */
	void logout();

	/**
	 * HTTP 요청에 쿠키로 토큰이 포함되어 있는지 확인합니다.
	 */
	boolean hasTokensInCookie(HttpServletRequest request);

	String oauthAuthorization(String registrationId);

}
