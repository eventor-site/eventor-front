package com.eventorfront.reporttype.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.reporttype.dto.ReportTypeDto;

public interface ReportTypeService {

	List<ReportTypeDto> getReportTypes();

	Page<ReportTypeDto> getReportTypes(Pageable pageable);

	ReportTypeDto getReportType(Long reportTypeId);

	void createReportType(ReportTypeDto request);

	void updateReportType(Long reportTypeId, ReportTypeDto request);

	void deleteReportType(Long reportTypeId);
}
