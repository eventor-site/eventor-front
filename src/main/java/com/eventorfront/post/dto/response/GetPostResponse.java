package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.eventorfront.image.dto.response.GetImageResponse;

import lombok.Builder;

@Builder
public record GetPostResponse(
	Long postId,
	String categoryName,
	String statusName,
	String writer,
	String writerGrade,
	String title,
	String content,
	Long recommendationCount,
	Long viewCount,
	LocalDateTime createdAt,
	Boolean isFixed,

	String link,

	LocalDateTime startTime,
	LocalDateTime endTime,
	String endType,

	String shoppingMall,
	String productName,
	Long price,

	List<GetImageResponse> images,
	Integer attachmentImageCount,
	Double totalSize,
	Boolean isAuthorized,
	Boolean isFavorite) {
}
