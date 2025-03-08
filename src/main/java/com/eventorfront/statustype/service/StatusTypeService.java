package com.eventorfront.statustype.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.statustype.dto.StatusTypeDto;

public interface StatusTypeService {

	List<StatusTypeDto> searchStatusTypes(String keyword);

	List<StatusTypeDto> getStatusTypes();

	Page<StatusTypeDto> getStatusTypes(Pageable pageable);

	StatusTypeDto getStatusType(Long statusTypeId);

	String createStatusType(StatusTypeDto request);

	String updateStatusType(Long statusTypeId, StatusTypeDto request);

	String deleteStatusType(Long statusTypeId);
}
