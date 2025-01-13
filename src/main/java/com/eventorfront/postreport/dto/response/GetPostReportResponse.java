package com.eventorfront.postreport.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record GetPostReportResponse(
	Long postReportId,
	Long postId,
	String identifier,
	String title,
	LocalDateTime createdAt,
	String reportTypeName) {
}