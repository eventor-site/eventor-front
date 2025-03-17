package com.eventorfront.image.exception;

import com.eventorfront.global.exception.ServerException;

import lombok.Getter;

@Getter
public class ImageConvertException extends ServerException {
	public ImageConvertException() {
		super("이미지 변환에 실패 하였습니다.");
	}
}
