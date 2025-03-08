package com.eventorfront.statustype.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.statustype.client.StatusTypeClient;
import com.eventorfront.statustype.dto.StatusTypeDto;
import com.eventorfront.statustype.service.StatusTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatusTypeServiceImpl implements StatusTypeService {
	private final StatusTypeClient statusTypeClient;

	@Override
	public List<StatusTypeDto> searchStatusTypes(String keyword) {
		return statusTypeClient.searchStatusTypes(keyword).getData();
	}

	@Override
	public StatusTypeDto getStatusType(Long statusTypeId) {
		return statusTypeClient.getStatusType(statusTypeId).getData();
	}

	@Override
	public List<StatusTypeDto> getStatusTypes() {
		return statusTypeClient.getStatusTypes().getData();
	}

	@Override
	public Page<StatusTypeDto> getStatusTypes(Pageable pageable) {
		return statusTypeClient.getStatusTypes(pageable).getData();
	}

	@Override
	public String createStatusType(StatusTypeDto request) {
		return statusTypeClient.createStatusType(request).getMessage();
	}

	@Override
	public String updateStatusType(Long statusTypeId, StatusTypeDto request) {
		return statusTypeClient.updateStatusType(statusTypeId, request).getMessage();
	}

	@Override
	public String deleteStatusType(Long statusTypeId) {
		return statusTypeClient.deleteStatusType(statusTypeId).getMessage();
	}
}
