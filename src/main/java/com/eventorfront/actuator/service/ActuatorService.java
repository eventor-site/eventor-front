package com.eventorfront.actuator.service;

import com.eventorfront.global.dto.ApiResponse;

public interface ActuatorService {

	ApiResponse<Boolean> backCheckHealth();

	ApiResponse<String> backCheckVersion();

}
