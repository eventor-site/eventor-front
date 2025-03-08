package com.eventorfront.postreport.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	public Page<GetPostReportResponse> getPostReports(Pageable pageable) {
		return postReportClient.getPostReports(pageable).getData();
	}

	@Override
	public String createPostReport(Long postId, String reportTypeName) {
		return postReportClient.createPostReport(postId, reportTypeName).getMessage();
	}

	@Override
	public String confirmPostReport(Long postId, Long postReportId) {
		return postReportClient.confirmPostReport(postId, postReportId).getMessage();
	}

	@Override
	public String deletePostReport(Long postReportId) {
		return postReportClient.deletePostReport(postReportId).getMessage();
	}
}
