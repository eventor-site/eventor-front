package com.eventorfront.status.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.status.client.StatusClient;
import com.eventorfront.status.dto.request.StatusRequest;
import com.eventorfront.status.dto.response.GetStatusResponse;
import com.eventorfront.status.service.StatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
	private final StatusClient statusClient;

	@Override
	public ApiResponse<List<GetStatusResponse>> getStatuses(String statusTypeName) {
		return statusClient.getStatuses(statusTypeName).getBody();
	}

	@Override
	public ApiResponse<Page<GetStatusResponse>> getStatuses(Pageable pageable) {
		return statusClient.getStatuses(pageable).getBody();
	}

	@Override
	public ApiResponse<GetStatusResponse> getStatus(Long statusId) {
		return statusClient.getStatus(statusId).getBody();
	}

	@Override
	public ApiResponse<Void> createStatus(StatusRequest request) {
		return statusClient.createStatus(request).getBody();
	}

	@Override
	public ApiResponse<Void> updateStatus(Long statusTypeId, StatusRequest request) {
		return statusClient.updateStatus(statusTypeId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteStatus(Long statusTypeId) {
		return statusClient.deleteStatus(statusTypeId).getBody();
	}
}
