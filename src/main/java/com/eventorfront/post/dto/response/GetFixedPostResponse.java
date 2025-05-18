package com.eventorfront.post.dto.response;

import lombok.Builder;

@Builder
public record GetFixedPostResponse(
	Long postId,
	String title) {
}