package com.eventorfront.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.eventorfront.global.exception.payload.ErrorStatus;

public class ForbiddenException extends GlobalException {
	public ForbiddenException() {
		super(ErrorStatus.from(
			String.format("현재 사용자는 접근 권한이 없습니다."),
			HttpStatus.FORBIDDEN,
			LocalDateTime.now()
		));
	}
}
