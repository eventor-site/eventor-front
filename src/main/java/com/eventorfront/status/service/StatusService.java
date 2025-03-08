package com.eventorfront.status.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.status.dto.request.StatusRequest;
import com.eventorfront.status.dto.response.GetStatusResponse;

public interface StatusService {

	List<GetStatusResponse> getStatuses(String statusTypeName);

	Page<GetStatusResponse> getStatuses(Pageable pageable);

	GetStatusResponse getStatus(Long statusId);

	String createStatus(StatusRequest request);

	String updateStatus(Long statusId, StatusRequest request);

	String deleteStatus(Long statusId);
}
