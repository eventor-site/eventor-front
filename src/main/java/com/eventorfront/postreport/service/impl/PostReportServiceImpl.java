package com.eventorfront.postreport.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.postreport.client.PostReportClient;
import com.eventorfront.postreport.dto.response.GetPostReportResponse;
import com.eventorfront.postreport.service.PostReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostReportServiceImpl implements PostReportService {
	private final PostReportClient postReportClient;

	@Override
	public List<GetPostReportResponse> getPostReports() {
		return postReportClient.getPostReports().getBody();
	}

	@Override
	public ResponseEntity<String> createPostReport(Long postId, String reportTypeName) {
		return postReportClient.createPostReport(postId, reportTypeName);
	}

	@Override
	public ResponseEntity<String> deletePostReport(Long postReportId) {
		return postReportClient.deletePostReport(postReportId);
	}
}
