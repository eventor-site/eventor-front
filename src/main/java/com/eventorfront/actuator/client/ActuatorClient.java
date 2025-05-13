package com.eventorfront.actuator.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "actuator-client", url = "${feignClient.url}")
public interface ActuatorClient {

	@GetMapping(value = "/back/actuator/health")
	ResponseEntity<ApiResponse<Boolean>> backCheckHealth();

	@GetMapping(value = "/back/actuator/version")
	ResponseEntity<ApiResponse<String>> backCheckVersion();

}
