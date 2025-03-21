package com.eventorfront.user.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.user.client.UserClient;
import com.eventorfront.user.dto.request.CertifyEmailRequest;
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
	public ApiResponse<Page<GetUserListResponse>> getUsers(Pageable pageable) {
		return userClient.getUsers(pageable).getBody();
	}

	@Override
	public ApiResponse<List<GetUserByIdentifier>> searchUserByIdentifier(String keyword) {
		return userClient.searchUserByIdentifier(keyword).getBody();
	}

	@Override
	public ApiResponse<List<GetUserByUserId>> searchUserByUserId(Long userId) {
		return userClient.searchUserByUserId(userId).getBody();
	}

	@Override
	public ApiResponse<GetUserResponse> getUser(Long userId) {
		return userClient.getUser(userId).getBody();
	}

	@Override
	public ApiResponse<GetUserResponse> getUserInfo() {
		return userClient.getUserInfo().getBody();
	}

	@Override
	public ApiResponse<Void> updateUserByAdmin(Long userId, UpdateUserRequest request) {
		return userClient.updateUserByAdmin(userId, request).getBody();
	}

	@Override
	public ApiResponse<Void> updateUserAttributeByAdmin(Long userId, UpdateUserAttributeRequest request) {
		return userClient.updateUserAttributeByAdmin(userId, request).getBody();
	}

	@Override
	public ApiResponse<Void> updateUser(UpdateUserRequest request) {
		return userClient.updateUser(request).getBody();
	}

	@Override
	public ApiResponse<Void> withdrawUser() {
		return userClient.withdrawUser().getBody();
	}

	@Override
	public ApiResponse<Boolean> meCheckRoles(String roleName) {
		return userClient.meCheckRoles(roleName).getBody();
	}

	@Override
	public ApiResponse<List<String>> meRoles() {
		return userClient.meRoles().getBody();
	}

	@Override
	public ApiResponse<Void> meCheckNickname(CheckNicknameRequest request) {
		return userClient.meCheckNickname(request).getBody();
	}

	@Override
	public ApiResponse<Void> modifyPassword(ModifyPasswordRequest request) {
		return userClient.modifyPassword(request).getBody();
	}

	@Override
	public ApiResponse<Void> signup(SignUpRequest signupRequest) {
		return userClient.signup(signupRequest).getBody();
	}

	@Override
	public ApiResponse<Void> checkIdentifier(CheckIdentifierRequest request) {
		return userClient.checkIdentifier(request).getBody();
	}

	@Override
	public ApiResponse<Void> checkNickname(CheckNicknameRequest request) {
		return userClient.checkNickname(request).getBody();
	}

	@Override
	public ApiResponse<Boolean> sendEmail(SendCodeRequest request) {
		return userClient.sendEmail(request).getBody();
	}

	@Override
	public ApiResponse<Boolean> certifyEmail(CertifyEmailRequest request) {
		return userClient.certifyEmail(request).getBody();
	}

	@Override
	public ApiResponse<Void> recoverIdentifier(String identifier) {
		return userClient.recoverIdentifier(identifier).getBody();
	}

	@Override
	public ApiResponse<Void> recoverPassword(String identifier) {
		return userClient.recoverPassword(identifier).getBody();
	}
}
