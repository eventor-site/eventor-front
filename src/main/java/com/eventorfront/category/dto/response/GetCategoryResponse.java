package com.eventorfront.category.dto.response;

import lombok.Builder;

@Builder
public record GetCategoryResponse(
	Long categoryId,
	String name,
	String parentCategoryName) {
}
