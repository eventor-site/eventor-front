package com.eventorfront.user.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.user.client.UserClient;
import com.eventorfront.user.dto.response.GetUserByAddShopResponse;
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
}
