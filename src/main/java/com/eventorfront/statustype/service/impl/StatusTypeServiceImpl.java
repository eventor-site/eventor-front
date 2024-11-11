package com.eventorfront.statustype.service.impl;

import java.util.List;

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
		return statusTypeClient.searchStatusTypes(keyword).getBody();
	}

	@Override
	public StatusTypeDto getStatusType(Long statusTypeId) {
		return statusTypeClient.getStatusType(statusTypeId).getBody();
	}

	@Override
	public List<StatusTypeDto> getStatusTypes() {
		return statusTypeClient.getStatusTypes().getBody();
	}

	@Override
	public void createStatusType(StatusTypeDto request) {
		statusTypeClient.createStatusType(request);
	}

	@Override
	public void updateStatusType(Long statusTypeId, StatusTypeDto request) {
		statusTypeClient.updateStatusType(statusTypeId, request);
	}

	@Override
	public void deleteStatusType(Long statusTypeId) {
		statusTypeClient.deleteStatusType(statusTypeId);
	}
}
