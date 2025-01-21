package com.eventorfront.userstop.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetUserStopResponse(
	Long userStopId,
	String identifier,
	String reportTypeName,
	Long stopDay,
	LocalDateTime startTime,
	LocalDateTime endTime) {

}
