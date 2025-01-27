package com.eventorfront.category.dto.request;

public record CreateCategoryRequest(
	String name,
	String parentCategoryName) {
}
