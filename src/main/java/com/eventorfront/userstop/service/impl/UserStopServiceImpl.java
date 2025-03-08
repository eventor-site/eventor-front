package com.eventorfront.userstop.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.userstop.client.UserStopClient;
import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopByUserIdResponse;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;
import com.eventorfront.userstop.service.UserStopService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStopServiceImpl implements UserStopService {
	private final UserStopClient userStopClient;

	@Override
	public ApiResponse<UserStopDto> getUserStop(Long userStopId) {
		return userStopClient.getUserStop(userStopId).getBody();
	}

	@Override
	public ApiResponse<Page<GetUserStopResponse>> getUserStops(Pageable pageable) {
		return userStopClient.getUserStops(pageable).getBody();
	}

	@Override
	public ApiResponse<List<GetUserStopByUserIdResponse>> getUserStopsByUserId(Long userId) {
		return userStopClient.getUserStopsByUserId(userId).getBody();
	}

	@Override
	public ApiResponse<Void> createUserStop(UserStopDto request) {
		return userStopClient.createUserStop(request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteUserStop(Long statusTypeId) {
		return userStopClient.deleteUserStop(statusTypeId).getBody();
	}
}
