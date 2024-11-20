package com.eventorfront.post.dto.request;

import lombok.Builder;

@Builder
public record CreatePostRequest(
	String categoryName,
	String title,
	String content,
	Boolean isNotification) {
}
