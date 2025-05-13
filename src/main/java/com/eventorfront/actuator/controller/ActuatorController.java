package com.eventorfront.actuator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ActuatorController {

	@Value("${server.port}")
	private int port;

	@GetMapping("/actuator/health")
	public ResponseEntity<Boolean> checkFrontHealth() {
		return ResponseEntity.ok(true);
	}

	@GetMapping("/actuator/version")
	public ResponseEntity<String> getFrontVersion() {
		String version;
		if (port == 8001 || port == 8002) {
			version = "blue";
		} else {
			version = "green";
		}

		return ResponseEntity.ok(version);
	}
}
