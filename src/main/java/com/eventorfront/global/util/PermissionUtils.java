package com.eventorfront.global.util;

import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public class PermissionUtils {
	public static final List<String> categories = List.of("핫딜", "자유");

	public static final List<String> bestFoodCategories = List.of(
		"맛집", "서울", "광역시", "8도", "제주도",
		"광주", "대구", "대전", "부산", "울산", "인천",
		"강원도", "경기도", "경남", "경북", "전남", "전북", "충남", "충북"
	);

	public static final List<String> memberCategories = Stream.concat(categories.stream(), bestFoodCategories.stream())
		.toList();

	private PermissionUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

}
