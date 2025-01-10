package com.eventorfront.reporttype.service;

import java.util.List;

import com.eventorfront.reporttype.dto.ReportTypeDto;

public interface ReportTypeService {

	List<ReportTypeDto> getReportTypes();

	ReportTypeDto getReportType(Long reportTypeId);

	void createReportType(ReportTypeDto request);

	void updateReportType(Long reportTypeId, ReportTypeDto request);

	void deleteReportType(Long reportTypeId);
}
