package com.eventorfront.postreport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.postreport.dto.response.GetPostReportResponse;

public interface PostReportService {

	Page<GetPostReportResponse> getPostReports(Pageable pageable);

	String createPostReport(Long postId, String reportTypeName);

	String confirmPostReport(Long postId, Long postReportId);

	String deletePostReport(Long postReportId);
}
