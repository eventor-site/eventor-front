package com.eventorfront.postreport.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.eventorfront.postreport.dto.response.GetPostReportResponse;

public interface PostReportService {

	Page<GetPostReportResponse> getPostReports(Pageable pageable);

	ResponseEntity<String> createPostReport(Long postId, String reportTypeName);

	void confirmPostReport(Long postId, Long postReportId);

	ResponseEntity<String> deletePostReport(Long postReportId);
}
