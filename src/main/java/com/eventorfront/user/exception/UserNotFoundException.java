package com.eventorfront.user.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.eventorfront.global.exception.NotFoundException;
import com.eventorfront.global.exception.payload.ErrorStatus;

public class UserNotFoundException extends NotFoundException {
	public UserNotFoundException(Object value) {
		super(
			ErrorStatus.from(String.format("해당 회원 '%s'는 존재하지 않는 회원 입니다.", value),
				HttpStatus.NOT_FOUND,
				LocalDateTime.now()));
	}
}
