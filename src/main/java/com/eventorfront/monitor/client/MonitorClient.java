package com.eventorfront.monitor.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "monitor-client", url = "${feignClient.url}")
public interface MonitorClient {

	@GetMapping(value = "/back/monitors/health")
	ResponseEntity<ApiResponse<Boolean>> backCheckHealth();

	@GetMapping(value = "/back/monitors/version")
	ResponseEntity<ApiResponse<String>> backCheckVersion();

}
