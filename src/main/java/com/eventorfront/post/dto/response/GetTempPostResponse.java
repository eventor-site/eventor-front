package com.eventorfront.post.dto.response;

import lombok.Builder;

@Builder
public record GetTempPostResponse(
	Long postId,
	String statusName
) {
}
