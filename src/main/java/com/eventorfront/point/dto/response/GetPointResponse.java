package com.eventorfront.point.dto.response;

public record GetPointResponse(
	Long pointId,
	String name,
	Long amount) {
}
