package com.eventorfront.pointhistory.point.dto.response;

import lombok.Builder;

@Builder
public record GetUserPointTotalResponse(
	Long userId,
	String nickname,
	String email,
	Long amount
) {
}
