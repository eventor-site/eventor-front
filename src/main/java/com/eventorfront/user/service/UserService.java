package com.eventorfront.user.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.global.dto.ApiResponse;
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

public interface UserService {

	ApiResponse<Page<GetUserListResponse>> getUsers(Pageable pageable);

	ApiResponse<List<GetUserByIdentifier>> searchUserByIdentifier(String keyword);

	ApiResponse<List<GetUserByUserId>> searchUserByUserId(Long userId);

	ApiResponse<GetUserResponse> getUser(Long userId);

	ApiResponse<GetUserResponse> getUserInfo();

	ApiResponse<Void> updateUserByAdmin(Long userId, UpdateUserRequest request);

	ApiResponse<Void> updateUserAttributeByAdmin(Long userId, UpdateUserAttributeRequest request);

	ApiResponse<Void> updateUser(UpdateUserRequest request);

	ApiResponse<Void> withdrawUser();

	ApiResponse<Boolean> meCheckRoles(String roleName);

	ApiResponse<List<String>> meRoles();

	ApiResponse<Void> meCheckNickname(CheckNicknameRequest request);

	ApiResponse<Void> modifyPassword(ModifyPasswordRequest request);

	ApiResponse<Void> signup(SignUpRequest signupRequest);

	ApiResponse<Void> checkIdentifier(CheckIdentifierRequest request);

	ApiResponse<Void> checkNickname(CheckNicknameRequest request);

	ApiResponse<Boolean> sendEmail(SendCodeRequest request);

	ApiResponse<Boolean> certifyEmail(CertifyEmailRequest request);

	ApiResponse<Void> recoverIdentifier(String identifier);

	ApiResponse<Void> recoverPassword(String identifier);

}
