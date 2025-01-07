package com.eventorfront.user.dto.request;

import lombok.Builder;

@Builder
public record ModifyPasswordRequest(
	String currentPassword,
	String newPassword
) {
}
