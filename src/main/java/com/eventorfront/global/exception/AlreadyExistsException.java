package com.eventorfront.global.exception;

import com.eventorfront.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class AlreadyExistsException extends GlobalException {
	public AlreadyExistsException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
