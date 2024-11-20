package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetPostResponse(
	Long postId,
	String categoryName,
	String writer,
	String title,
	String content,
	Long recommendationCount,
	Long viewCount,
	LocalDateTime createdAt,
	Boolean isNotification,
	Boolean isWriter) {
}
