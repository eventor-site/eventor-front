package com.eventorfront.reporttype.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.reporttype.dto.ReportTypeDto;

@FeignClient(name = "reportType-client", url = "${feignClient.url}")
public interface ReportTypeClient {

	@GetMapping("/back/reportTypes")
	ResponseEntity<ApiResponse<List<ReportTypeDto>>> getReportTypes();

	@GetMapping("/back/reportTypes/paging")
	ResponseEntity<ApiResponse<Page<ReportTypeDto>>> getReportTypes(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/reportTypes/{reportTypeId}")
	ResponseEntity<ApiResponse<ReportTypeDto>> getReportType(@PathVariable Long reportTypeId);

	@PostMapping("/back/reportTypes")
	ResponseEntity<ApiResponse<Void>> createReportType(@RequestBody ReportTypeDto request);

	@PutMapping("/back/reportTypes/{reportTypeId}")
	ResponseEntity<ApiResponse<Void>> updateReportType(@PathVariable Long reportTypeId,
		@RequestBody ReportTypeDto request);

	@DeleteMapping("/back/reportTypes/{reportTypeId}")
	ResponseEntity<ApiResponse<Void>> deleteReportType(@PathVariable Long reportTypeId);
}
