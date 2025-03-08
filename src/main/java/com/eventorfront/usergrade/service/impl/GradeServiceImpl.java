package com.eventorfront.usergrade.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.usergrade.client.GradeClient;
import com.eventorfront.usergrade.dto.GradeDto;
import com.eventorfront.usergrade.service.GradeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
	private final GradeClient gradeClient;

	@Override
	public ApiResponse<List<GradeDto>> getGrades() {
		return gradeClient.getGrades().getBody();
	}

	@Override
	public ApiResponse<GradeDto> getGrade(Long gradeId) {
		return gradeClient.getGrade(gradeId).getBody();
	}

	@Override
	public ApiResponse<Page<GradeDto>> getGrades(Pageable pageable) {
		return gradeClient.getGrades(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createGrade(GradeDto request) {
		return gradeClient.createGrade(request).getBody();
	}

	@Override
	public ApiResponse<Void> updateGrade(Long statusTypeId, GradeDto request) {
		return gradeClient.updateGrade(statusTypeId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteGrade(Long statusTypeId) {
		return gradeClient.deleteGrade(statusTypeId).getBody();
	}
}
