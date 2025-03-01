package com.eventorfront.status.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	public List<GetStatusResponse> getStatuses(String statusTypeName) {
		return statusClient.getStatuses(statusTypeName).getBody();
	}

	@Override
	public Page<GetStatusResponse> getStatuses(Pageable pageable) {
		return statusClient.getStatuses(pageable).getBody();
	}

	@Override
	public GetStatusResponse getStatus(Long statusId) {
		return statusClient.getStatus(statusId).getBody();
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
