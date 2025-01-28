package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record ApiResponse(
	String status,
	String message,
	LocalDateTime serverTime,
	Boolean isAuthorized,
	Object data
) {
}
