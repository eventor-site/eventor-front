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
		return statusClient.getStatuses(statusTypeName).getData();
	}

	@Override
	public Page<GetStatusResponse> getStatuses(Pageable pageable) {
		return statusClient.getStatuses(pageable).getData();
	}

	@Override
	public GetStatusResponse getStatus(Long statusId) {
		return statusClient.getStatus(statusId).getData();
	}

	@Override
	public String createStatus(StatusRequest request) {
		return statusClient.createStatus(request).getMessage();
	}

	@Override
	public String updateStatus(Long statusTypeId, StatusRequest request) {
		return statusClient.updateStatus(statusTypeId, request).getMessage();
	}

	@Override
	public String deleteStatus(Long statusTypeId) {
		return statusClient.deleteStatus(statusTypeId).getMessage();
	}
}
