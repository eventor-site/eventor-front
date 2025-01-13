package com.eventorfront.user.dto.response;

import lombok.Builder;

@Builder
public record GetUserByIdentifier(
	String identifier) {
}
