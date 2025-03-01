package com.eventorfront.user.dto.request;

import lombok.Builder;

@Builder
public record UpdateUserAttributeRequest(
	Long statusId,
	Long gradeId
) {
}
