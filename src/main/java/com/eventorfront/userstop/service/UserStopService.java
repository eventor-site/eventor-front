package com.eventorfront.userstop.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopByUserIdResponse;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;

public interface UserStopService {

	Page<GetUserStopResponse> getUserStops(Pageable pageable);

	List<GetUserStopByUserIdResponse> getUserStopsByUserId(Long userId);

	UserStopDto getUserStop(Long userStopId);

	String createUserStop(UserStopDto request);

	String deleteUserStop(Long userStopId);
}
