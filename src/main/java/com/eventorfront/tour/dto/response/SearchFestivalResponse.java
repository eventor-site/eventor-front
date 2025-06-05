package com.eventorfront.tour.dto.response;

import lombok.Builder;

@Builder
public record SearchFestivalResponse(
	String contentId,         // 내용 ID
	String contentTypeId,     // 내용 타입 ID
	String title,            // 축제 제목
	String addr1,            // 기본 주소
	String addr2,            // 상세 주소 또는 장소명
	String eventStartDate,   // 시작일 (yyyyMMdd)
	String eventEndDate,     // 종료일 (yyyyMMdd)
	String firstImage,       // 대표 이미지 URL
	String tel,               // 전화번호
	String eventStatusName,   // 상태: 예정, 진행중, 마감, 미정
	Integer remainingDay      // 남은 일수 (오늘 기준 종료일까지 D-day)
) {
}