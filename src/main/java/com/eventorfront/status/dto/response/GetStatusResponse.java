package com.eventorfront.status.dto.response;

public record GetStatusResponse(
	Long statusId,
	String name,
	String statusTypeName) {
}
