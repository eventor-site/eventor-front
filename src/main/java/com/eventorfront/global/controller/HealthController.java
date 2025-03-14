package com.eventorfront.global.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eventorfront.global.client.HealthClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HealthController {
	private final HealthClient healthClient;

	@Value("${server.port}")
	private int port;

	@GetMapping("/checks/health")
	public Boolean checkHealth() {
		return true;
	}

	@GetMapping("/checks/version")
	public String checkVersion() {
		if (port == 8001 || port == 8002) {
			return "blue";
		} else {
			return "green";
		}
	}

	@GetMapping("/back/checks/health")
	public Boolean checkBackHealth() {
		return healthClient.backCheckHealth();
	}

	@GetMapping("/back/checks/version")
	public String checkBackVersion() {
		return healthClient.backCheckVersion();
	}

}
