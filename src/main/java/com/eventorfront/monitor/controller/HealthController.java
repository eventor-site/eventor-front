package com.eventorfront.monitor.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventorfront.monitor.service.MonitorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthController {
	private final MonitorService monitorService;

	@Value("${server.port}")
	private int port;

	@GetMapping("/actuator/health")
	public Boolean checkHealth() {
		return true;
	}

	@GetMapping("/actuator/version")
	public String checkVersion() {
		if (port == 8001 || port == 8002) {
			return "blue";
		} else {
			return "green";
		}
	}

	@GetMapping("/back/actuator/health")
	public Boolean checkBackHealth() {
		return monitorService.backCheckHealth().getData();
	}

	@GetMapping("/back/actuator/version")
	public String checkBackVersion() {
		return monitorService.backCheckVersion().getData();
	}

}
