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

import com.eventorfront.statustype.dto.StatusTypeDto;

@FeignClient(name = "statusType-client", url = "http://localhost:8090/back/statusTypes")
public interface StatusTypeClient {

	@GetMapping("/search")
	ResponseEntity<List<StatusTypeDto>> searchStatusTypes(@RequestParam String keyword);

	@GetMapping
	ResponseEntity<List<StatusTypeDto>> getStatusTypes();

	@GetMapping("/paging")
	ResponseEntity<Page<StatusTypeDto>> getStatusTypes(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/{statusTypeId}")
	ResponseEntity<StatusTypeDto> getStatusType(@PathVariable Long statusTypeId);

	@PostMapping
	ResponseEntity<Void> createStatusType(
		@RequestBody StatusTypeDto request);

	@PutMapping("/{statusTypeId}")
	ResponseEntity<Void> updateStatusType(@PathVariable Long statusTypeId,
		@RequestBody StatusTypeDto request);

	@DeleteMapping("/{statusTypeId}")
	ResponseEntity<Void> deleteStatusType(@PathVariable Long statusTypeId);
}
