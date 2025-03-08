package com.eventorfront.statustype.client;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.statustype.dto.StatusTypeDto;

@FeignClient(name = "statusType-client", url = "${feignClient.url}")
public interface StatusTypeClient {

	@GetMapping("/back/statusTypes/search")
	ResponseEntity<ApiResponse<List<StatusTypeDto>>> searchStatusTypes(@RequestParam String keyword);

	@GetMapping("/back/statusTypes")
	ResponseEntity<ApiResponse<List<StatusTypeDto>>> getStatusTypes();

	@GetMapping("/back/statusTypes/paging")
	ResponseEntity<ApiResponse<Page<StatusTypeDto>>> getStatusTypes(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/statusTypes/{statusTypeId}")
	ResponseEntity<ApiResponse<StatusTypeDto>> getStatusType(@PathVariable Long statusTypeId);

	@PostMapping("/back/statusTypes")
	ResponseEntity<ApiResponse<Void>> createStatusType(
		@RequestBody StatusTypeDto request);

	@PutMapping("/back/statusTypes/{statusTypeId}")
	ResponseEntity<ApiResponse<Void>> updateStatusType(@PathVariable Long statusTypeId,
		@RequestBody StatusTypeDto request);

	@DeleteMapping("/back/statusTypes/{statusTypeId}")
	ResponseEntity<ApiResponse<Void>> deleteStatusType(@PathVariable Long statusTypeId);
}
