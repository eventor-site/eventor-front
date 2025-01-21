package com.eventorfront.postreport.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.postreport.dto.response.GetPostReportResponse;

public interface PostReportService {

	List<GetPostReportResponse> getPostReports();

	ResponseEntity<String> createPostReport(Long postId, String reportTypeName);

	void confirmPostReport(Long postId, Long postReportId);

	ResponseEntity<String> deletePostReport(Long postReportId);
}
