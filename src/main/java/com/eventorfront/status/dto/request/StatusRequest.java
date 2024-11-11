package com.eventorfront.status.dto.request;

public record StatusRequest(
	String name,
	Long statusTypeId) {
}
