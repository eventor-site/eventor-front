package com.eventorfront.statistic.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "statistic-client", url = "${feignClient.url}")
public interface StatisticClient {

	@PostMapping("/back/statistics/visitors")
	ResponseEntity<ApiResponse<Void>> saveVisitor(@RequestParam String uuid);

}
