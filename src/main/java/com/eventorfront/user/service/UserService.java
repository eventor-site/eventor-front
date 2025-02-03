package com.eventorfront.user.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.SendCodeRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserByUserId;
import com.eventorfront.user.dto.response.GetUserResponse;

public interface UserService {
	List<GetUserByIdentifier> searchUserByIdentifier(String keyword);

	List<GetUserByUserId> searchUserByUserId(Long userId);

	GetUserResponse getUserInfo();

	void updateUser(UpdateUserRequest request);

	void withdrawUser();

	boolean meCheckRoles(String roleName);

	List<String> meRoles();

	ResponseEntity<String> meCheckNickname(CheckNicknameRequest request);

	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	ResponseEntity<Void> signup(SignUpRequest signupRequest);

	ResponseEntity<String> checkIdentifier(CheckIdentifierRequest request);

	ResponseEntity<String> checkNickname(CheckNicknameRequest request);

	ResponseEntity<String> sendEmail(SendCodeRequest request);

	ResponseEntity<String> checkEmail(String email, String certifyCode);

	ResponseEntity<String> recoverIdentifier(String email);

	ResponseEntity<String> recoverPassword(String identifier);

}
