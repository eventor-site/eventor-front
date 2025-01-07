package com.eventorfront.user.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByAddShopResponse;
import com.eventorfront.user.dto.response.GetUserResponse;

public interface UserService {
	List<GetUserByAddShopResponse> searchUserById(String keyword);

	GetUserResponse getUserInfo();

	void updateUser(UpdateUserRequest request);

	ResponseEntity<String> modifyPassword(ModifyPasswordRequest request);

	void withdrawUser();

}
