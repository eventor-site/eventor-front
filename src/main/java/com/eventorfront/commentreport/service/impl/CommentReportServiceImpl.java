package com.eventorfront.commentreport.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
		return commentReportClient.getCommentReports(pageable).getBody();
	}

	@Override
	public ResponseEntity<String> createCommentReport(Long commentId, String reportTypeName) {
		return commentReportClient.createCommentReport(commentId, reportTypeName);
	}

	@Override
	public void confirmCommentReport(Long postId, Long commentId, Long commentReportId) {
		commentReportClient.confirmCommentReport(postId, commentId, commentReportId);
	}

	@Override
	public ResponseEntity<String> deleteCommentReport(Long commentReportId) {
		return commentReportClient.deleteCommentReport(commentReportId);
	}
}
