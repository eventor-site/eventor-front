package com.eventorfront.actuator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventorfront.actuator.service.ActuatorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ActuatorController {
	private final ActuatorService actuatorService;

	@Value("${server.port}")
	private int port;

	@GetMapping("/actuator/health")
	public Boolean checkFrontHealth() {
		return true;
	}

	@GetMapping("/actuator/version")
	public String getFrontVersion() {
		if (port == 8001 || port == 8002) {
			return "blue";
		} else {
			return "green";
		}
	}

	// @GetMapping("/back/actuator/health")
	// public Boolean checkBackendHealth() {
	// 	return actuatorService.backCheckHealth().getData();
	// }
	//
	// @GetMapping("/back/actuator/version")
	// public String checkBackVersion() {
	// 	return actuatorService.backCheckVersion().getData();
	// }

}
