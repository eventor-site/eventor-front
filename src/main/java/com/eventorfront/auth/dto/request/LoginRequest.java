package com.eventorfront.auth.dto.request;

public record LoginRequest(
	String identifier,
	String password) {
}
