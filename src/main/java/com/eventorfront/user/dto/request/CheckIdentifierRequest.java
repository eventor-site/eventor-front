package com.eventorfront.user.dto.request;

import lombok.Builder;

@Builder
public record CheckIdentifierRequest(
	String identifier
) {
}
