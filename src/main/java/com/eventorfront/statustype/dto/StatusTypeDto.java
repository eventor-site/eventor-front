package com.eventorfront.statustype.dto;

import lombok.Builder;

@Builder
public record StatusTypeDto(
	Long statusTypeId,
	String name) {
}
