package com.eventorfront.usergrade.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sikyeojofront.usergrade.client.UserGradeClient;
import com.sikyeojofront.usergrade.dto.UserGradeDto;
import com.sikyeojofront.usergrade.service.UserGradeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserGradeServiceImpl implements UserGradeService {
	private final UserGradeClient userGradeClient;

	@Override
	public UserGradeDto getUserGrade(Long userGradeId) {
		return userGradeClient.getUserGrade(userGradeId).getBody();
	}

	@Override
	public List<UserGradeDto> getUserGrades() {
		return userGradeClient.getUserGrades().getBody();
	}

	@Override
	public void createUserGrade(UserGradeDto request) {
		userGradeClient.createUserGrade(request);
	}

	@Override
	public void updateUserGrade(Long statusTypeId, UserGradeDto request) {
		userGradeClient.updateUserGrade(statusTypeId, request);
	}

	@Override
	public void deleteUserGrade(Long statusTypeId) {
		userGradeClient.deleteUserGrade(statusTypeId);
	}
}
