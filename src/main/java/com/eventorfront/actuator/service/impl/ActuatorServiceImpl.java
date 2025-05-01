package com.eventorfront.actuator.service.impl;

import org.springframework.stereotype.Service;

import com.eventorfront.actuator.client.ActuatorClient;
import com.eventorfront.actuator.service.ActuatorService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActuatorServiceImpl implements ActuatorService {
	private final ActuatorClient actuatorClient;

	@Override
	public ApiResponse<Boolean> backCheckHealth() {
		return actuatorClient.backCheckHealth().getBody();
	}

	@Override
	public ApiResponse<String> backCheckVersion() {
		return actuatorClient.backCheckVersion().getBody();
	}
}
