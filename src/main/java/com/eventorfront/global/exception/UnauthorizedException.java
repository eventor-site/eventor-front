package com.eventorfront.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.eventorfront.global.exception.payload.ErrorStatus;

public class UnauthorizedException extends GlobalException {
	public UnauthorizedException() {
		super(ErrorStatus.from(
			"로그인이 필요합니다.",
			HttpStatus.UNAUTHORIZED,
			LocalDateTime.now()
		));
	}
}
