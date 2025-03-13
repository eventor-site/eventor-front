package com.eventorfront.global.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checks")
public class HealthController {

	@Value("${server.port}")
	private int port;

	@GetMapping("/health")
	public Boolean checkHealth() {
		return true;
	}

	@GetMapping("/version")
	public String checkVersion() {
		if (port == 8001 || port == 8002) {
			return "blue";
		} else {
			return "green";
		}
	}

}
