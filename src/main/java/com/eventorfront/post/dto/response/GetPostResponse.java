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
	String writerGrade,
	String title,
	String link,
	String shoppingMall,
	String productName,
	Long price,
	String content,
	Long recommendationCount,
	Long viewCount,
	LocalDateTime createdAt,
	LocalDateTime startTime,
	LocalDateTime endTime,
	String statusName,
	List<GetImageResponse> images,
	Double totalSize,
	Boolean isAuthorized,
	Boolean isFavorite) {
}
