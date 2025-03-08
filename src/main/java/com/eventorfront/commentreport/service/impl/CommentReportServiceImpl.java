package com.eventorfront.commentreport.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.commentreport.client.CommentReportClient;
import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;
import com.eventorfront.commentreport.service.CommentReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentReportServiceImpl implements CommentReportService {
	private final CommentReportClient commentReportClient;

	@Override
	public Page<GetCommentReportResponse> getCommentReports(Pageable pageable) {
		return commentReportClient.getCommentReports(pageable).getData();
	}

	@Override
	public String createCommentReport(Long commentId, String reportTypeName) {
		return commentReportClient.createCommentReport(commentId, reportTypeName).getMessage();
	}

	@Override
	public String confirmCommentReport(Long postId, Long commentId, Long commentReportId) {
		return commentReportClient.confirmCommentReport(postId, commentId, commentReportId).getMessage();
	}

	@Override
	public String deleteCommentReport(Long commentReportId) {
		return commentReportClient.deleteCommentReport(commentReportId).getMessage();
	}
}
