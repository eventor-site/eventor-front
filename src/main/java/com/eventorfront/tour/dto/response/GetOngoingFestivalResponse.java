package com.eventorfront.tour.dto.response;

import lombok.Builder;

@Builder
public record GetOngoingFestivalResponse(
	String contentId,         // 내용 ID
	String contentTypeId,     // 내용 타입 ID
	String title,            // 축제 제목
	String firstImage
) {
}