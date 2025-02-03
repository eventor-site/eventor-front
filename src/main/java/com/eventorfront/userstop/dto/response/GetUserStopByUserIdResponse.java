package com.eventorfront.userstop.dto.response;

import lombok.Builder;

@Builder
public record GetUserStopByUserIdResponse(
	String reportTypeName,
	Long reportCount
) {
}
