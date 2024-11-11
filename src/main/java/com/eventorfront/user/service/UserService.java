package com.eventorfront.user.service;

import java.util.List;

import com.sikyeojofront.user.dto.response.GetUserByAddShopResponse;

public interface UserService {
	List<GetUserByAddShopResponse> searchUserById(String keyword);
}
