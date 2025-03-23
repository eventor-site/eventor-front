package com.eventorfront.global.util;

import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;

@Getter
public class CategoryUtils {
	public static final List<String> categories = List.of("핫딜", "자유");

	public static final List<String> bestFoodCategories = List.of(
		"맛집", "서울", "광역시", "8도", "제주도",
		"광주", "대구", "대전", "부산", "울산", "인천",
		"강원도", "경기도", "경남", "경북", "전남", "전북", "충남", "충북"
	);

	public static final List<String> memberCategories = Stream.concat(categories.stream(), bestFoodCategories.stream())
		.toList();

	public static final List<String> eventCategories = List.of(
		"이벤트", "전체",
		// 음식
		"음식", "한·양·중·일 식", "치킨", "피자", "패스트푸드", "카페",
		// 패션
		"패션", "상의", "하의", "아우터", "신발", "액세서리", "계절 컬렉션",
		// 뷰티
		"뷰티", "스킨케어", "메이크업", "향수", "남성 화장품", "기능성 화장품",
		// 전자제품
		"전자제품", "가전제품", "모바일기기", "PC 및 주변기기", "TV", "건강 및 미용가전",
		// 축제
		"축제", "계절", "불꽃", "먹거리", "빛", "꽃", "음악", "지역축제",
		// 엔터
		"엔터", "콘서트", "굿즈", "팝업스토어", "공연", "스포츠",
		// 금융
		"금융", "신용카드", "체크카드", "예·적금"
	);

	private CategoryUtils() {
		throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
	}

}
