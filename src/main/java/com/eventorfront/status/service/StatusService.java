package com.eventorfront.status.service;

import java.util.List;

import com.sikyeojofront.status.dto.request.StatusRequest;
import com.sikyeojofront.status.dto.response.GetStatusResponse;

public interface StatusService {

	List<GetStatusResponse> getStatusesByStatusTypeName(String statusTypeName);

	List<GetStatusResponse> getStatuses();

	GetStatusResponse getStatus(Long statusId);

	void createStatus(StatusRequest request);

	void updateStatus(Long statusId, StatusRequest request);

	void deleteStatus(Long statusId);
}
