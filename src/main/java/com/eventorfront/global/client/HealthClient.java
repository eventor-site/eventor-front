package com.eventorfront.global.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "health-client", url = "${feignClient.url}")
public interface HealthClient {

	@GetMapping(value = "/back/checks/health")
	Boolean backCheckHealth();

	@GetMapping(value = "/back/checks/version")
	String backCheckVersion();

}
