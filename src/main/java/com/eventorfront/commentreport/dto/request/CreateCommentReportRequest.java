package com.eventorfront.commentreport.dto.request;

import lombok.Builder;

@Builder
public record CreateCommentReportRequest(
	Long commentId,
	String reportType) {
}
