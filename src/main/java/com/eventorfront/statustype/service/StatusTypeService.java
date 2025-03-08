package com.eventorfront.statustype.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.statustype.dto.StatusTypeDto;

public interface StatusTypeService {

	ApiResponse<List<StatusTypeDto>> searchStatusTypes(String keyword);

	ApiResponse<List<StatusTypeDto>> getStatusTypes();

	ApiResponse<Page<StatusTypeDto>> getStatusTypes(Pageable pageable);

	ApiResponse<StatusTypeDto> getStatusType(Long statusTypeId);

	ApiResponse<Void> createStatusType(StatusTypeDto request);

	ApiResponse<Void> updateStatusType(Long statusTypeId, StatusTypeDto request);

	ApiResponse<Void> deleteStatusType(Long statusTypeId);
}
