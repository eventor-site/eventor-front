package com.eventorfront.usergrade.service.impl;

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
	public GradeDto getGrade(Long gradeId) {
		return gradeClient.getGrade(gradeId).getBody();
	}

	@Override
	public Page<GradeDto> getGrades(Pageable pageable) {
		return gradeClient.getGrades(pageable).getBody();
	}

	@Override
	public void createGrade(GradeDto request) {
		gradeClient.createGrade(request);
	}

	@Override
	public void updateGrade(Long statusTypeId, GradeDto request) {
		gradeClient.updateGrade(statusTypeId, request);
	}

	@Override
	public void deleteGrade(Long statusTypeId) {
		gradeClient.deleteGrade(statusTypeId);
	}
}
