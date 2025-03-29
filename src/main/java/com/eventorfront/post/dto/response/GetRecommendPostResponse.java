package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetRecommendPostResponse(
	Long postId,
	String title,
	String writer,
	Long recommendationCount,
	Long viewCount,
	LocalDateTime createdAt,
	String imageUrl,
	String imageType) {
}