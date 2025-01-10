package com.eventorfront.user.dto.request;

import lombok.Builder;

@Builder
public record FindIdentifierRequest(
	String identifier
) {
}
