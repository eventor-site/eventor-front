package com.eventorfront.commentreport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;

public interface CommentReportService {

	Page<GetCommentReportResponse> getCommentReports(Pageable pageable);

	ResponseEntity<String> createCommentReport(Long commentId, String reportTypeName);

	void confirmCommentReport(Long postId, Long commentId, Long commentReportId);

	ResponseEntity<String> deleteCommentReport(Long commentReportId);
}
