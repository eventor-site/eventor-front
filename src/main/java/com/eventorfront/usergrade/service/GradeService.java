package com.eventorfront.usergrade.service;

import java.util.List;

import com.eventorfront.usergrade.dto.GradeDto;

public interface GradeService {

	List<GradeDto> getGrades();

	GradeDto getGrade(Long gradeId);

	void createGrade(GradeDto request);

	void updateGrade(Long gradeId, GradeDto request);

	void deleteGrade(Long gradeId);
}
