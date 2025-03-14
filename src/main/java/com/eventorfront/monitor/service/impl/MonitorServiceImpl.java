package com.eventorfront.monitor.service.impl;

import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.monitor.client.MonitorClient;
import com.eventorfront.monitor.service.MonitorService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {
	private final MonitorClient monitorClient;

	@Override
	public ApiResponse<Boolean> backCheckHealth() {
		return monitorClient.backCheckHealth().getBody();
	}

	@Override
	public ApiResponse<String> backCheckVersion() {
		return monitorClient.backCheckVersion().getBody();
	}
}
