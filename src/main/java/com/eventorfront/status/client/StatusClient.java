package com.eventorfront.status.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.sikyeojofront.status.dto.request.StatusRequest;
import com.sikyeojofront.status.dto.response.GetStatusResponse;

@FeignClient(name = "status-client", url = "http://localhost:8090/back/statuses")
public interface StatusClient {

	@GetMapping("/types")
	ResponseEntity<List<GetStatusResponse>> getStatusesByStatusTypeName(@RequestParam String statusTypeName);

	@GetMapping
	ResponseEntity<List<GetStatusResponse>> getStatuses();

	@GetMapping("/{statusId}")
	ResponseEntity<GetStatusResponse> getStatus(@PathVariable Long statusId);

	@PostMapping
	ResponseEntity<Void> createStatus(@RequestBody StatusRequest request);

	@PutMapping("/{statusId}")
	ResponseEntity<Void> updateStatus(@PathVariable Long statusId, @RequestBody StatusRequest request);

	@DeleteMapping("/{statusId}")
	ResponseEntity<Void> deleteStatus(@PathVariable Long statusId);
}
