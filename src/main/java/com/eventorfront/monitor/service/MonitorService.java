package com.eventorfront.monitor.service;

import com.eventorfront.global.dto.ApiResponse;

public interface MonitorService {

	ApiResponse<Boolean> backCheckHealth();

	ApiResponse<String> backCheckVersion();

}
