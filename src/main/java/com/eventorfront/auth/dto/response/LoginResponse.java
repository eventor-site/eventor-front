package com.eventorfront.auth.dto.response;

public record LoginResponse(
	String accessToken,
	String refreshToken,
	String userStatusName) {
}
