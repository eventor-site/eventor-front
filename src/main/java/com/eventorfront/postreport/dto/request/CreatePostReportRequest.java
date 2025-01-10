package com.eventorfront.postreport.dto.request;

import lombok.Builder;

@Builder
public record CreatePostReportRequest(
	Long postId,
	String reportType) {
}
