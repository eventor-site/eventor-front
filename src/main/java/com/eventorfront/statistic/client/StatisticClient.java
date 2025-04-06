package com.eventorfront.statistic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.statistic.dto.response.GetStatistic;

@FeignClient(name = "statistic-client", url = "${feignClient.url}")
public interface StatisticClient {

	@GetMapping("/back/statistics")
	ResponseEntity<ApiResponse<Page<GetStatistic>>> getStatistics(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/back/statistics/visitors")
	ResponseEntity<ApiResponse<Void>> saveVisitor(@RequestParam String uuid);

}
