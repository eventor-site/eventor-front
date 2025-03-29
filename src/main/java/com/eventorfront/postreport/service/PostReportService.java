package com.eventorfront.postreport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.postreport.dto.response.GetPostReportResponse;

public interface PostReportService {

	ApiResponse<Page<GetPostReportResponse>> getPostReports(Pageable pageable);

	ApiResponse<Void> createPostReport(Long postId, String reportTypeName);

	ApiResponse<Void> confirmPostReport(Long postReportId);

	ApiResponse<Void> deletePostReport(Long postReportId);
}
