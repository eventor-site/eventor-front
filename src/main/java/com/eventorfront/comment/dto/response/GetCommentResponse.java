package com.eventorfront.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record GetCommentResponse(
	Long commentId,
	Long parentCommentId,
	String writer,
	String content,
	Long recommendationCount,
	Long decommendationCount,
	List<GetCommentResponse> childComments,
	LocalDateTime createdAt,
	Boolean isAuthorized) {
}
