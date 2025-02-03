package com.eventorfront.usergrade.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.usergrade.dto.GradeDto;

public interface GradeService {

	List<GradeDto> getGrades();

	Page<GradeDto> getGrades(Pageable pageable);

	GradeDto getGrade(Long gradeId);

	void createGrade(GradeDto request);

	void updateGrade(Long gradeId, GradeDto request);

	void deleteGrade(Long gradeId);
}
