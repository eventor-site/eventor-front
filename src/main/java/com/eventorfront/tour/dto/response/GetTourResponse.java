package com.eventorfront.tour.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record GetTourResponse(
	// 공통 정보
	String contentId,
	String contentTypeId,
	String title,
	String overview,
	String tel,
	String homepage,
	String address,
	String zipcode,
	String mapX,
	String mapY,

	// 소개 정보
	String infoCenter,
	String openDate,
	String restDate,
	String useTime,
	String parking,
	String babyCarriage,
	String petAllowed,
	String creditCard,

	// 부가 정보
	String admission,
	String toilet,

	// 이미지 리스트
	List<String> images
) {
}
