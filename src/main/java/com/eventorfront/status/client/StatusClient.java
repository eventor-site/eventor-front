package com.eventorfront.status.client;

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

import com.eventorfront.status.dto.request.StatusRequest;
import com.eventorfront.status.dto.response.GetStatusResponse;

@FeignClient(name = "status-client", url = "${feignClient.url}")
public interface StatusClient {

	@GetMapping("/back/statuses")
	ResponseEntity<List<GetStatusResponse>> getStatuses(@RequestParam String statusTypeName);

	@GetMapping("/back/statuses/paging")
	ResponseEntity<Page<GetStatusResponse>> getStatuses(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/statuses/{statusId}")
	ResponseEntity<GetStatusResponse> getStatus(@PathVariable Long statusId);

	@PostMapping("/back/statuses")
	ResponseEntity<Void> createStatus(@RequestBody StatusRequest request);

	@PutMapping("/back/statuses/{statusId}")
	ResponseEntity<Void> updateStatus(@PathVariable Long statusId, @RequestBody StatusRequest request);

	@DeleteMapping("/back/statuses/{statusId}")
	ResponseEntity<Void> deleteStatus(@PathVariable Long statusId);
}
