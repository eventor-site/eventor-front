package com.eventorfront.reporttype.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.reporttype.client.ReportTypeClient;
import com.eventorfront.reporttype.dto.ReportTypeDto;
import com.eventorfront.reporttype.service.ReportTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportTypeServiceImpl implements ReportTypeService {
	private final ReportTypeClient reportTypeClient;

	@Override
	public ApiResponse<ReportTypeDto> getReportType(Long reportTypeId) {
		return reportTypeClient.getReportType(reportTypeId).getBody();
	}

	@Override
	public ApiResponse<List<ReportTypeDto>> getReportTypes() {
		return reportTypeClient.getReportTypes().getBody();
	}

	@Override
	public ApiResponse<Page<ReportTypeDto>> getReportTypes(Pageable pageable) {
		return reportTypeClient.getReportTypes(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createReportType(ReportTypeDto request) {
		return reportTypeClient.createReportType(request).getBody();
	}

	@Override
	public ApiResponse<Void> updateReportType(Long reportTypeId, ReportTypeDto request) {
		return reportTypeClient.updateReportType(reportTypeId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteReportType(Long reportTypeId) {
		return reportTypeClient.deleteReportType(reportTypeId).getBody();
	}
}
