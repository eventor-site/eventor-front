package com.eventorfront.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.eventorfront.global.exception.AlreadyExistsException;
import com.eventorfront.global.exception.payload.ErrorStatus;

public class UserAlreadyExistsException extends AlreadyExistsException {
	public UserAlreadyExistsException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 회원 '%s'는 이미 존재 하는 회원 입니다.", value),
				HttpStatus.CONFLICT,
				LocalDateTime.now()));
	}
}
