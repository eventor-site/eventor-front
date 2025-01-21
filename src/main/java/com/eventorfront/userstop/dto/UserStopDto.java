package com.eventorfront.userstop.dto;

import lombok.Builder;

@Builder
public record UserStopDto(
	String identifier,
	Long reportTypeId,
	Long stopDay) {
}
