package com.eventorfront.status.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.status.dto.request.StatusRequest;
import com.eventorfront.status.dto.response.GetStatusResponse;

public interface StatusService {

	ApiResponse<List<GetStatusResponse>> getStatuses(String statusTypeName);

	ApiResponse<Page<GetStatusResponse>> getStatuses(Pageable pageable);

	ApiResponse<GetStatusResponse> getStatus(Long statusId);

	ApiResponse<Void> createStatus(StatusRequest request);

	ApiResponse<Void> updateStatus(Long statusId, StatusRequest request);

	ApiResponse<Void> deleteStatus(Long statusId);
}
