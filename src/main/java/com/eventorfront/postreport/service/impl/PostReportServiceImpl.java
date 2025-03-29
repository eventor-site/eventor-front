package com.eventorfront.postreport.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.postreport.client.PostReportClient;
import com.eventorfront.postreport.dto.response.GetPostReportResponse;
import com.eventorfront.postreport.service.PostReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostReportServiceImpl implements PostReportService {
	private final PostReportClient postReportClient;

	@Override
	public ApiResponse<Page<GetPostReportResponse>> getPostReports(Pageable pageable) {
		return postReportClient.getPostReports(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createPostReport(Long postId, String reportTypeName) {
		return postReportClient.createPostReport(postId, reportTypeName).getBody();
	}

	@Override
	public ApiResponse<Void> confirmPostReport(Long postReportId) {
		return postReportClient.confirmPostReport(postReportId).getBody();
	}

	@Override
	public ApiResponse<Void> deletePostReport(Long postReportId) {
		return postReportClient.deletePostReport(postReportId).getBody();
	}
}
