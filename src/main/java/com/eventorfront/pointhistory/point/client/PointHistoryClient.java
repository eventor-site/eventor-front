package com.eventorfront.pointhistory.point.client;

import java.time.LocalDateTime;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.pointhistory.point.dto.response.GetUserPointTotalResponse;

@FeignClient(name = "point-history-client", url = "${feignClient.url}")
public interface PointHistoryClient {

	@GetMapping("/back/pointHistories/paging")
	ResponseEntity<ApiResponse<Page<GetUserPointTotalResponse>>> getUserPointTotalsByPeriod(
		@RequestParam(required = false) LocalDateTime startDate, @RequestParam(required = false) LocalDateTime endDate,
		@PageableDefault(page = 1, size = 10) Pageable pageable);

}
