package com.eventorfront.usergrade.service;

import java.util.List;

import com.sikyeojofront.usergrade.dto.UserGradeDto;

public interface UserGradeService {

	List<UserGradeDto> getUserGrades();

	UserGradeDto getUserGrade(Long userGradeId);

	void createUserGrade(UserGradeDto request);

	void updateUserGrade(Long userGradeId, UserGradeDto request);

	void deleteUserGrade(Long userGradeId);
}
