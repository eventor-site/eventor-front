package com.eventorfront.user.dto.response;

import lombok.Builder;

@Builder
public record GetUserByUserId(
	Long userId,
	String nickname) {
}
