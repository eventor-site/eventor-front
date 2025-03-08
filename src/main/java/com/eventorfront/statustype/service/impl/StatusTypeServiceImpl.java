package com.eventorfront.statustype.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.statustype.client.StatusTypeClient;
import com.eventorfront.statustype.dto.StatusTypeDto;
import com.eventorfront.statustype.service.StatusTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusTypeServiceImpl implements StatusTypeService {
	private final StatusTypeClient statusTypeClient;

	@Override
	public ApiResponse<List<StatusTypeDto>> searchStatusTypes(String keyword) {
		return statusTypeClient.searchStatusTypes(keyword).getBody();
	}

	@Override
	public ApiResponse<StatusTypeDto> getStatusType(Long statusTypeId) {
		return statusTypeClient.getStatusType(statusTypeId).getBody();
	}

	@Override
	public ApiResponse<List<StatusTypeDto>> getStatusTypes() {
		return statusTypeClient.getStatusTypes().getBody();
	}

	@Override
	public ApiResponse<Page<StatusTypeDto>> getStatusTypes(Pageable pageable) {
		return statusTypeClient.getStatusTypes(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createStatusType(StatusTypeDto request) {
		return statusTypeClient.createStatusType(request).getBody();
	}

	@Override
	public ApiResponse<Void> updateStatusType(Long statusTypeId, StatusTypeDto request) {
		return statusTypeClient.updateStatusType(statusTypeId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteStatusType(Long statusTypeId) {
		return statusTypeClient.deleteStatusType(statusTypeId).getBody();
	}
}
