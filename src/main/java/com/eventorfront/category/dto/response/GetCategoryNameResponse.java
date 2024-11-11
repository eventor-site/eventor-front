package com.eventorfront.category.dto.response;

import lombok.Builder;

@Builder
public record GetCategoryNameResponse(
	Long categoryId,
	String name) {
}
