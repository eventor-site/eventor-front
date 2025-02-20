package com.eventorfront.image.dto.response;

import lombok.Builder;

@Builder
public record GetImageResponse(
	Long imageId,
	String originalName,
	String url,
	Long size,
	Boolean isThumbnail
) {
}
