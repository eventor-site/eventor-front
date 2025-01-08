package com.eventorfront.bookmark.dto.request;

import lombok.Builder;

@Builder
public record CreateBookmarkRequest(
	Long categoryId) {
}
