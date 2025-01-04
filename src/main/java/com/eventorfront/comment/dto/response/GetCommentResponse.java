package com.eventorfront.comment.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetCommentResponse(
	Long commentId,
	Long parentCommentId,
	String writer,
	String content,
	Long recommendationCount,
	Long decommendationCount,
	LocalDateTime createdAt) {
}
