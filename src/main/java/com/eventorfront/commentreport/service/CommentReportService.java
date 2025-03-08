package com.eventorfront.commentreport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;

public interface CommentReportService {

	Page<GetCommentReportResponse> getCommentReports(Pageable pageable);

	String createCommentReport(Long commentId, String reportTypeName);

	String confirmCommentReport(Long postId, Long commentId, Long commentReportId);

	String deleteCommentReport(Long commentReportId);
}
