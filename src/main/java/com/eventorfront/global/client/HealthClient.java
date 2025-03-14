package com.eventorfront.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "health-client", url = "${feignClient.url}")
public interface HealthClient {

	@GetMapping(value = "/back/checks/health")
	ResponseEntity<ApiResponse<Boolean>> backCheckHealth();

	@GetMapping(value = "/back/checks/version")
	ResponseEntity<ApiResponse<String>> backCheckVersion();

}
