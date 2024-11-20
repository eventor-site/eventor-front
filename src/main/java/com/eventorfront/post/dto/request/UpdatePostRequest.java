package com.eventorfront.post.dto.request;

import lombok.Builder;

@Builder
public record UpdatePostRequest(
	String title,
	String content,
	Boolean isNotification) {
}
