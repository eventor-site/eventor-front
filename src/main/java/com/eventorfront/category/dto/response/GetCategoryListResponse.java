package com.eventorfront.category.dto.response;

import lombok.Builder;

@Builder
public record GetCategoryListResponse(
	Long categoryId,
	String name,
	Long depth) {
}
