package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetPostsByCategoryNameResponse(
	Long postId,
	String writer,
	String title,
	Long recommendationCount,
	Long viewCount,
	LocalDateTime createdAt,
	String gradeName,
	String imageUrl) {
}
