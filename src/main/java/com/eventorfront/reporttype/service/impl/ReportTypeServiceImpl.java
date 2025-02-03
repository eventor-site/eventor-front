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
		return reportTypeClient.getReportType(reportTypeId).getBody();
	}

	@Override
	public List<ReportTypeDto> getReportTypes() {
		return reportTypeClient.getReportTypes().getBody();
	}

	@Override
	public Page<ReportTypeDto> getReportTypes(Pageable pageable) {
		return reportTypeClient.getReportTypes(pageable).getBody();
	}

	@Override
	public void createReportType(ReportTypeDto request) {
		reportTypeClient.createReportType(request);
	}

	@Override
	public void updateReportType(Long reportTypeId, ReportTypeDto request) {
		reportTypeClient.updateReportType(reportTypeId, request);
	}

	@Override
	public void deleteReportType(Long reportTypeId) {
		reportTypeClient.deleteReportType(reportTypeId);
	}
}
