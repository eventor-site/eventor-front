package com.eventorfront.category.dto.request;

import lombok.Builder;

@Builder
public record UpdateCategoryRequest(
	String name,
	Long parentCategoryId) {
}
