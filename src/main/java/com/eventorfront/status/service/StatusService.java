package com.eventorfront.status.service;

import java.util.List;

import com.eventorfront.status.dto.request.StatusRequest;
import com.eventorfront.status.dto.response.GetStatusResponse;

public interface StatusService {

	List<GetStatusResponse> getStatusesByStatusTypeName(String statusTypeName);

	List<GetStatusResponse> getStatuses();

	GetStatusResponse getStatus(Long statusId);

	void createStatus(StatusRequest request);

	void updateStatus(Long statusId, StatusRequest request);

	void deleteStatus(Long statusId);
}
