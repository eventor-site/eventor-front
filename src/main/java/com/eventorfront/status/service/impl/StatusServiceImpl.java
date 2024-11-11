package com.eventorfront.status.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sikyeojofront.status.client.StatusClient;
import com.sikyeojofront.status.dto.request.StatusRequest;
import com.sikyeojofront.status.dto.response.GetStatusResponse;
import com.sikyeojofront.status.service.StatusService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {
	private final StatusClient statusClient;

	@Override
	public List<GetStatusResponse> getStatusesByStatusTypeName(String statusTypeName) {
		return statusClient.getStatusesByStatusTypeName(statusTypeName).getBody();
	}

	@Override
	public GetStatusResponse getStatus(Long statusId) {
		return statusClient.getStatus(statusId).getBody();
	}

	@Override
	public List<GetStatusResponse> getStatuses() {
		return statusClient.getStatuses().getBody();
	}

	@Override
	public void createStatus(StatusRequest request) {
		statusClient.createStatus(request);
	}

	@Override
	public void updateStatus(Long statusTypeId, StatusRequest request) {
		statusClient.updateStatus(statusTypeId, request);
	}

	@Override
	public void deleteStatus(Long statusTypeId) {
		statusClient.deleteStatus(statusTypeId);
	}
}
