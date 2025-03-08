package com.eventorfront.usergrade.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.usergrade.dto.GradeDto;

public interface GradeService {

	ApiResponse<List<GradeDto>> getGrades();

	ApiResponse<Page<GradeDto>> getGrades(Pageable pageable);

	ApiResponse<GradeDto> getGrade(Long gradeId);

	ApiResponse<Void> createGrade(GradeDto request);

	ApiResponse<Void> updateGrade(Long gradeId, GradeDto request);

	ApiResponse<Void> deleteGrade(Long gradeId);
}
