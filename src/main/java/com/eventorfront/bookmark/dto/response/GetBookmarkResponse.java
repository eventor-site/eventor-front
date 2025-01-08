package com.eventorfront.bookmark.dto.response;

import lombok.Builder;

@Builder
public record GetBookmarkResponse(
	Long bookmarkId,
	String categoryName) {
}

