package com.eventorfront.global.exception;

import com.eventorfront.global.exception.payload.ErrorStatus;

import lombok.Getter;

@Getter
public class NotFoundException extends GlobalException {
	public NotFoundException(ErrorStatus errorStatus) {
		super(errorStatus);
	}
}
