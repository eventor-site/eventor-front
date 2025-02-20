package com.eventorfront.point.client;

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

import com.eventorfront.point.dto.request.PointRequest;
import com.eventorfront.point.dto.response.GetPointResponse;

@FeignClient(name = "point-client", url = "${feignClient.url}")
public interface PointClient {

	@GetMapping("/back/points/paging")
	ResponseEntity<Page<GetPointResponse>> getPoints(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/points/{pointId}")
	ResponseEntity<GetPointResponse> getPoint(@PathVariable Long pointId);

	@PostMapping("/back/points")
	ResponseEntity<Void> createPoint(@RequestBody PointRequest request);

	@PutMapping("/back/points/{pointId}")
	ResponseEntity<Void> updatePoint(@PathVariable Long pointId, @RequestBody PointRequest request);

	@DeleteMapping("/back/points/{pointId}")
	ResponseEntity<Void> deletePoint(@PathVariable Long pointId);
}
