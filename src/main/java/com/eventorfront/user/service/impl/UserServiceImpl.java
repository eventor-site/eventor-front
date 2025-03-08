package com.eventorfront.user.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.user.client.UserClient;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.SendCodeRequest;
import com.eventorfront.user.dto.request.UpdateUserAttributeRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserByUserId;
import com.eventorfront.user.dto.response.GetUserListResponse;
import com.eventorfront.user.dto.response.GetUserResponse;
import com.eventorfront.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserClient userClient;

	@Override
	public Page<GetUserListResponse> getUsers(Pageable pageable) {
		return userClient.getUsers(pageable).getData();
	}

	@Override
	public List<GetUserByIdentifier> searchUserByIdentifier(String keyword) {
		return userClient.searchUserByIdentifier(keyword).getData();
	}

	@Override
	public List<GetUserByUserId> searchUserByUserId(Long userId) {
		return userClient.searchUserByUserId(userId).getData();
	}

	@Override
	public GetUserResponse getUser(Long userId) {
		return userClient.getUser(userId).getData();
	}

	@Override
	public GetUserResponse getUserInfo() {
		return userClient.getUserInfo().getData();
	}

	@Override
	public void updateUserByAdmin(Long userId, UpdateUserRequest request) {
		userClient.updateUserByAdmin(userId, request);
	}

	@Override
	public void updateUserAttributeByAdmin(Long userId, UpdateUserAttributeRequest request) {
		userClient.updateUserAttributeByAdmin(userId, request);
	}

	@Override
	public String updateUser(UpdateUserRequest request) {
		return userClient.updateUser(request).getMessage();
	}

	@Override
	public String withdrawUser() {
		return userClient.withdrawUser().getMessage();
	}

	@Override
	public boolean meCheckRoles(String roleName) {
		return Boolean.TRUE.equals(userClient.meCheckRoles(roleName).getData());
	}

	@Override
	public List<String> meRoles() {
		return userClient.meRoles().getData();
	}

	@Override
	public String meCheckNickname(CheckNicknameRequest request) {
		return userClient.meCheckNickname(request).getMessage();
	}

	@Override
	public String modifyPassword(ModifyPasswordRequest request) {
		return userClient.modifyPassword(request).getMessage();
	}

	@Override
	public String signup(SignUpRequest signupRequest) {
		return userClient.signup(signupRequest).getMessage();
	}

	@Override
	public String checkIdentifier(CheckIdentifierRequest request) {
		return userClient.checkIdentifier(request).getMessage();
	}

	@Override
	public String checkNickname(CheckNicknameRequest request) {
		return userClient.checkNickname(request).getMessage();
	}

	@Override
	public String sendEmail(SendCodeRequest request) {
		return userClient.sendEmail(request).getMessage();
	}

	@Override
	public String checkEmail(String email, String certifyCode) {
		return userClient.checkEmail(email, certifyCode).getMessage();
	}

	@Override
	public String recoverIdentifier(String email) {
		return userClient.recoverIdentifier(email).getMessage();
	}

	@Override
	public String recoverPassword(String identifier) {
		return userClient.recoverPassword(identifier).getMessage();
	}
}
