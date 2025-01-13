package com.eventorfront.userstop.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.userstop.client.UserStopClient;
import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;
import com.eventorfront.userstop.service.UserStopService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserStopServiceImpl implements UserStopService {
	private final UserStopClient userStopClient;

	@Override
	public UserStopDto getUserStop(Long userStopId) {
		return userStopClient.getUserStop(userStopId).getBody();
	}

	@Override
	public List<GetUserStopResponse> getUserStops() {
		return userStopClient.getUserStops().getBody();
	}

	@Override
	public void createUserStop(UserStopDto request) {
		userStopClient.createUserStop(request);
	}

	@Override
	public void updateUserStop(Long statusTypeId, UserStopDto request) {
		userStopClient.updateUserStop(statusTypeId, request);
	}

	@Override
	public void deleteUserStop(Long statusTypeId) {
		userStopClient.deleteUserStop(statusTypeId);
	}
}
