package com.eventorfront.reporttype.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.reporttype.client.ReportTypeClient;
import com.eventorfront.reporttype.dto.ReportTypeDto;
import com.eventorfront.reporttype.service.ReportTypeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportTypeServiceImpl implements ReportTypeService {
	private final ReportTypeClient reportTypeClient;

	@Override
	public ReportTypeDto getReportType(Long reportTypeId) {
		return reportTypeClient.getReportType(reportTypeId).getData();
	}

	@Override
	public List<ReportTypeDto> getReportTypes() {
		return reportTypeClient.getReportTypes().getData();
	}

	@Override
	public Page<ReportTypeDto> getReportTypes(Pageable pageable) {
		return reportTypeClient.getReportTypes(pageable).getData();
	}

	@Override
	public String createReportType(ReportTypeDto request) {
		return reportTypeClient.createReportType(request).getMessage();
	}

	@Override
	public String updateReportType(Long reportTypeId, ReportTypeDto request) {
		return reportTypeClient.updateReportType(reportTypeId, request).getMessage();
	}

	@Override
	public String deleteReportType(Long reportTypeId) {
		return reportTypeClient.deleteReportType(reportTypeId).getMessage();
	}
}
