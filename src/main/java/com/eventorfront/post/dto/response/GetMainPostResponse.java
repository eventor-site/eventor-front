package com.eventorfront.post.dto.response;

import lombok.Builder;

@Builder
public record GetMainPostResponse(
	Long postId,
	String title,
	String imageUrl,
	String imageType) {
}