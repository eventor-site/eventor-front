package com.eventorfront.userstop.dto;

import lombok.Builder;

@Builder
public record UserStopDto(
	Long userId,
	Long reportTypeId,
	Long stopDay) {
}
