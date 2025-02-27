package com.eventorfront.post.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreatePostRequest(
	String categoryName,
	String title,
	String content,

	LocalDateTime startTime,
	LocalDateTime endTime,

	String link,
	String shoppingMall,
	String productName,
	Long price) {
}
