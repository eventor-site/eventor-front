package com.eventorfront.reporttype.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.reporttype.dto.ReportTypeDto;

public interface ReportTypeService {

	List<ReportTypeDto> getReportTypes();

	Page<ReportTypeDto> getReportTypes(Pageable pageable);

	ReportTypeDto getReportType(Long reportTypeId);

	String createReportType(ReportTypeDto request);

	String updateReportType(Long reportTypeId, ReportTypeDto request);

	String deleteReportType(Long reportTypeId);
}
