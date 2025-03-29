package com.eventorfront.image.dto.request;

import java.util.List;

import lombok.Builder;

@Builder
public record DeleteImageRequest(
	Long postId,
	String categoryName,
	List<Long> imageIds
) {
}
