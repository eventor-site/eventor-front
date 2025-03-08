package com.eventorfront.userstop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopByUserIdResponse;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;

public interface UserStopService {

	ApiResponse<Page<GetUserStopResponse>> getUserStops(Pageable pageable);

	ApiResponse<List<GetUserStopByUserIdResponse>> getUserStopsByUserId(Long userId);

	ApiResponse<UserStopDto> getUserStop(Long userStopId);

	ApiResponse<Void> createUserStop(UserStopDto request);

	ApiResponse<Void> deleteUserStop(Long userStopId);
}
