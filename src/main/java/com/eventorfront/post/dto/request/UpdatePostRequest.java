package com.eventorfront.post.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record UpdatePostRequest(
	String categoryName,
	String title,
	String content,

	String link,

	LocalDateTime startTime,
	LocalDateTime endTime,
	String endType,

	String shoppingMall,
	String productName,
	Long price
) {
}
