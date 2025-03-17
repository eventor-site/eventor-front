package com.eventorfront.post.dto.response;

import lombok.Builder;

@Builder
public record GetEventPostCountByAdminResponse(
	String nickname,
	Long eventPostCount) {
}