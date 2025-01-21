package com.eventorfront.userstop.service;

import java.util.List;

import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopByIdentifierResponse;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;

public interface UserStopService {

	List<GetUserStopResponse> getUserStops();

	List<GetUserStopByIdentifierResponse> getUserStopsByIdentifier(String identifier);

	UserStopDto getUserStop(Long userStopId);

	void createUserStop(UserStopDto request);

	void updateUserStop(Long userStopId, UserStopDto request);

	void deleteUserStop(Long userStopId);
}
