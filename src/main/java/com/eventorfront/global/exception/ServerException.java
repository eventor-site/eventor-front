package com.eventorfront.global.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.eventorfront.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class ServerException extends GlobalException {
	public ServerException() {
		super(ErrorStatus.from(
			"서버 에러",
			HttpStatus.INTERNAL_SERVER_ERROR,
			LocalDateTime.now())
		);
	}

	public ServerException(String message) {
		super(ErrorStatus.from(
			message,
			HttpStatus.INTERNAL_SERVER_ERROR,
			LocalDateTime.now())
		);
	}
}
