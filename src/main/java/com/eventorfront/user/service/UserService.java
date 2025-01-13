package com.eventorfront.user.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserResponse;

public interface UserService {
	List<GetUserByIdentifier> searchUserByIdentifier(String keyword);

	GetUserResponse getUserInfo();

	void updateUser(UpdateUserRequest request);

	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	ResponseEntity<String> checkIdentifier(CheckIdentifierRequest request);

	void withdrawUser();

	List<GetCommentResponse> getCommentsByUserId();

	ResponseEntity<Void> signup(SignUpRequest signupRequest);

	ResponseEntity<String> sendEmail(String email);

	ResponseEntity<String> checkEmail(String email, String certifyCode);

	ResponseEntity<String> recoverIdentifier(String email);

	ResponseEntity<String> recoverPassword(String identifier);

}
