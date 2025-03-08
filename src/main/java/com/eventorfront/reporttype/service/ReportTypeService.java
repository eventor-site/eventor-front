package com.eventorfront.reporttype.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.reporttype.dto.ReportTypeDto;

public interface ReportTypeService {

	ApiResponse<List<ReportTypeDto>> getReportTypes();

	ApiResponse<Page<ReportTypeDto>> getReportTypes(Pageable pageable);

	ApiResponse<ReportTypeDto> getReportType(Long reportTypeId);

	ApiResponse<Void> createReportType(ReportTypeDto request);

	ApiResponse<Void> updateReportType(Long reportTypeId, ReportTypeDto request);

	ApiResponse<Void> deleteReportType(Long reportTypeId);
}
