package com.eventorfront.search.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;

@Builder
public record SearchPostsResponse(
	Long postId,
	String categoryName,
	String statusName,
	String writer,
	String writerGrade,
	String title,
	String content,
	Long recommendationCount,
	Long viewCount,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
	LocalDateTime createdAt,

	String productName,
	String shoppingMall,

	String eventStatusName,
	Integer remainingDay,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
	LocalDateTime startTime,
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
	LocalDateTime endTime,
	String endType,

	String imageUrl
) {
}
