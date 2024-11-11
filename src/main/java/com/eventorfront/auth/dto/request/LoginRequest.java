package com.eventorfront.auth.dto.request;

public record LoginRequest(
	String id,
	String password) {
}
