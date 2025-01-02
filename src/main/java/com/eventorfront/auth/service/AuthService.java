package com.eventorfront.auth.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;

import com.eventorfront.auth.dto.request.LoginRequest;
import com.eventorfront.auth.dto.request.SignUpRequest;
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
	 * 사용자의 회원가입 요청을 처리합니다.
	 */
	ResponseEntity<Void> signUp(SignUpRequest signUpRequest);

	/**
	 * 회원가입을 위해 인증 이메일을 전송합니다.
	 */
	ResponseEntity<Void> sendEmailSignUp(String email);

	/**
	 * HTTP 요청에 쿠키로 토큰이 포함되어 있는지 확인합니다.
	 */
	boolean hasTokensInCookie(HttpServletRequest request);

	/**
	 * 사용자의 마지막 로그인 시간을 업데이트합니다.
	 */
	void updateLastLoginAt(String accessToken, String refreshToken, LocalDateTime lastLoginAt);

	/**
	 * 회원가입을 위한 이메일 인증 코드를 확인합니다.
	 */
	ResponseEntity<Void> checkEmailSignUp(String email, String certifyCode);

}
