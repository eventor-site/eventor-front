package com.eventorfront.commentreport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;
import com.eventorfront.global.dto.ApiResponse;

public interface CommentReportService {

	ApiResponse<Page<GetCommentReportResponse>> getCommentReports(Pageable pageable);

	ApiResponse<Void> createCommentReport(Long commentId, String reportTypeName);

	ApiResponse<Void> confirmCommentReport(Long postId, Long commentId, Long commentReportId);

	ApiResponse<Void> deleteCommentReport(Long commentReportId);
}
