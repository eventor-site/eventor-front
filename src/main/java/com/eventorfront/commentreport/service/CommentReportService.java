package com.eventorfront.commentreport.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;

public interface CommentReportService {

	List<GetCommentReportResponse> getCommentReports();

	ResponseEntity<String> createCommentReport(Long commentId, String reportTypeName);

	ResponseEntity<String> deleteCommentReport(Long commentReportId);
}
