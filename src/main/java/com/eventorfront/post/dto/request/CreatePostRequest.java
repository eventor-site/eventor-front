package com.eventorfront.post.dto.request;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record CreatePostRequest(
	String categoryName,
	String title,
	String content,
	Boolean isNotification,
	LocalDateTime startTime,
	LocalDateTime endTime) {
}
