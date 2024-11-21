package com.eventorfront.image.dto.response;

import lombok.Builder;

@Builder
public record GetImageResponse(
	String originalName,
	String url
) {
}
