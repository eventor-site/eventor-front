package com.eventorfront.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.auth.dto.request.SignUpRequest;
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

public interface UserService {

	Page<GetUserListResponse> getUsers(Pageable pageable);

	List<GetUserByIdentifier> searchUserByIdentifier(String keyword);

	List<GetUserByUserId> searchUserByUserId(Long userId);

	GetUserResponse getUser(Long userId);

	GetUserResponse getUserInfo();

	void updateUserByAdmin(Long userId, UpdateUserRequest request);

	void updateUserAttributeByAdmin(Long userId, UpdateUserAttributeRequest request);

	String updateUser(UpdateUserRequest request);

	String withdrawUser();

	boolean meCheckRoles(String roleName);

	List<String> meRoles();

	String meCheckNickname(CheckNicknameRequest request);

	String modifyPassword(ModifyPasswordRequest request);

	String signup(SignUpRequest signupRequest);

	String checkIdentifier(CheckIdentifierRequest request);

	String checkNickname(CheckNicknameRequest request);

	String sendEmail(SendCodeRequest request);

	String checkEmail(String email, String certifyCode);

	String recoverIdentifier(String email);

	String recoverPassword(String identifier);

}
