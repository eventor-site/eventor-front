package com.eventorfront.user.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.auth.dto.request.UpdateLastLoginTimeRequest;
import com.eventorfront.user.client.UserClient;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByAddShopResponse;
import com.eventorfront.user.dto.response.GetUserResponse;
import com.eventorfront.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserClient userClient;

	@Override
	public List<GetUserByAddShopResponse> searchUserById(String keyword) {
		return userClient.searchUserById(keyword).getBody();
	}

	@Override
	public GetUserResponse getUserInfo() {
		return userClient.getUserInfo().getBody();
	}

	@Override
	public void updateUser(UpdateUserRequest request) {
		userClient.updateUser(request);
	}

	@Override
	public void updateLastLoginTime(UpdateLastLoginTimeRequest request) {
		userClient.updateLastLoginTime(request);
	}

	@Override
	public ResponseEntity<String> modifyPassword(ModifyPasswordRequest request) {
		return userClient.modifyPassword(request);
	}
}
