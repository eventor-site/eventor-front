package com.eventorfront.user.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.user.client.UserClient;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.SendCodeRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserResponse;
import com.eventorfront.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
	private final UserClient userClient;

	@Override
	public List<GetUserByIdentifier> searchUserByIdentifier(String keyword) {
		return userClient.searchUserByIdentifier(keyword).getBody();
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
	public void withdrawUser() {
		userClient.withdrawUser();
	}

	@Override
	public boolean meCheckRoles(String roleName) {
		return Boolean.TRUE.equals(userClient.meCheckRoles(roleName).getBody());
	}

	@Override
	public List<String> meRoles() {
		return userClient.meRoles().getBody();
	}

	@Override
	public ResponseEntity<String> meCheckNickname(CheckNicknameRequest request) {
		return userClient.meCheckNickname(request);
	}

	@Override
	public ResponseEntity<String> modifyPassword(ModifyPasswordRequest request) {
		return userClient.modifyPassword(request);
	}

	@Override
	public ResponseEntity<Void> signup(SignUpRequest signupRequest) {
		return userClient.signup(signupRequest);
	}

	@Override
	public ResponseEntity<String> checkIdentifier(CheckIdentifierRequest request) {
		return userClient.checkIdentifier(request);
	}

	@Override
	public ResponseEntity<String> checkNickname(CheckNicknameRequest request) {
		return userClient.checkNickname(request);
	}

	@Override
	public ResponseEntity<String> sendEmail(SendCodeRequest request) {
		return userClient.sendEmail(request);
	}

	@Override
	public ResponseEntity<String> checkEmail(String email, String certifyCode) {
		return userClient.checkEmail(email, certifyCode);
	}

	@Override
	public ResponseEntity<String> recoverIdentifier(String email) {
		return userClient.recoverIdentifier(email);
	}

	@Override
	public ResponseEntity<String> recoverPassword(String identifier) {
		return userClient.recoverPassword(identifier);
	}
}
