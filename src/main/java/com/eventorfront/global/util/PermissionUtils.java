package com.eventorfront.global.util;

import java.util.List;

import lombok.Getter;

@Getter
public class PermissionUtils {
	public static final List<String> categories = List.of("핫딜", "자유", "맛집");

	private PermissionUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

}
