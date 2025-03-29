package com.eventorfront.commentreport.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.commentreport.client.CommentReportClient;
import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;
import com.eventorfront.commentreport.service.CommentReportService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentReportServiceImpl implements CommentReportService {
	private final CommentReportClient commentReportClient;

	@Override
	public ApiResponse<Page<GetCommentReportResponse>> getCommentReports(Pageable pageable) {
		return commentReportClient.getCommentReports(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createCommentReport(Long commentId, String reportTypeName) {
		return commentReportClient.createCommentReport(commentId, reportTypeName).getBody();
	}

	@Override
	public ApiResponse<Void> confirmCommentReport(Long commentReportId) {
		return commentReportClient.confirmCommentReport(commentReportId).getBody();
	}

	@Override
	public ApiResponse<Void> deleteCommentReport(Long commentReportId) {
		return commentReportClient.deleteCommentReport(commentReportId).getBody();
	}
}
