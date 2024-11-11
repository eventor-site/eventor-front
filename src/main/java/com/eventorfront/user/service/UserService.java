package com.eventorfront.user.service;

import java.util.List;

import com.eventorfront.user.dto.response.GetUserByAddShopResponse;

public interface UserService {
	List<GetUserByAddShopResponse> searchUserById(String keyword);
}
