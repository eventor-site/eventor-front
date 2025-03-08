package com.eventorfront.usergrade.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.usergrade.client.GradeClient;
import com.eventorfront.usergrade.dto.GradeDto;
import com.eventorfront.usergrade.service.GradeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
	private final GradeClient gradeClient;

	@Override
	public List<GradeDto> getGrades() {
		return gradeClient.getGrades().getData();
	}

	@Override
	public GradeDto getGrade(Long gradeId) {
		return gradeClient.getGrade(gradeId).getData();
	}

	@Override
	public Page<GradeDto> getGrades(Pageable pageable) {
		return gradeClient.getGrades(pageable).getData();
	}

	@Override
	public String createGrade(GradeDto request) {
		return gradeClient.createGrade(request).getMessage();
	}

	@Override
	public String updateGrade(Long statusTypeId, GradeDto request) {
		return gradeClient.updateGrade(statusTypeId, request).getMessage();
	}

	@Override
	public String deleteGrade(Long statusTypeId) {
		return gradeClient.deleteGrade(statusTypeId).getMessage();
	}
}
