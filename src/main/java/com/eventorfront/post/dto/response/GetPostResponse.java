package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.eventorfront.image.dto.response.GetImageResponse;

import lombok.Builder;

@Builder
public record GetPostResponse(
	Long postId,
	String categoryName,
	String writer,
	String title,
	String content,
	Long recommendationCount,
	Long viewCount,
	LocalDateTime createdAt,
	Boolean isNotification,
	List<GetImageResponse> images,
	Boolean isAuthorized) {
}
