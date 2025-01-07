package com.eventorfront.auth.dto.response;

import java.time.LocalDateTime;

public record LoginResponse(
	String accessToken,
	String refreshToken,
	LocalDateTime lastLoginTime) {
}
