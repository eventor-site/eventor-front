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

import com.eventorfront.reporttype.dto.ReportTypeDto;

@FeignClient(name = "reportType-client", url = "http://localhost:8090/back/reportTypes")
public interface ReportTypeClient {

	@GetMapping
	ResponseEntity<List<ReportTypeDto>> getReportTypes();

	@GetMapping("/paging")
	ResponseEntity<Page<ReportTypeDto>> getReportTypes(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/{reportTypeId}")
	ResponseEntity<ReportTypeDto> getReportType(@PathVariable Long reportTypeId);

	@PostMapping
	ResponseEntity<Void> createReportType(@RequestBody ReportTypeDto request);

	@PutMapping("/{reportTypeId}")
	ResponseEntity<Void> updateReportType(@PathVariable Long reportTypeId, @RequestBody ReportTypeDto request);

	@DeleteMapping("/{reportTypeId}")
	ResponseEntity<Void> deleteReportType(@PathVariable Long reportTypeId);
}
