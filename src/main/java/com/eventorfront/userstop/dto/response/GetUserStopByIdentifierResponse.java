package com.eventorfront.userstop.dto.response;

import lombok.Builder;

@Builder
public record GetUserStopByIdentifierResponse(
	String reportTypeName,
	Long reportCount
) {
}
