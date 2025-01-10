package com.eventorfront.postreport.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.postreport.dto.response.GetPostReportResponse;

public interface PostReportService {

	List<GetPostReportResponse> getPostReports();

	ResponseEntity<String> createPostReport(Long postId, String reportTypeName);

	ResponseEntity<String> deletePostReport(Long postReportId);
}
