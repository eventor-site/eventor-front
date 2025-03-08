package com.eventorfront.point.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.point.client.PointClient;
import com.eventorfront.point.dto.request.PointRequest;
import com.eventorfront.point.dto.response.GetPointResponse;
import com.eventorfront.point.service.PointService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {
	private final PointClient pointClient;

	@Override
	public ApiResponse<Page<GetPointResponse>> getPoints(Pageable pageable) {
		return pointClient.getPoints(pageable).getBody();
	}

	@Override
	public ApiResponse<GetPointResponse> getPoint(Long pointId) {
		return pointClient.getPoint(pointId).getBody();
	}

	@Override
	public ApiResponse<Void> createPoint(PointRequest request) {
		return pointClient.createPoint(request).getBody();
	}

	@Override
	public ApiResponse<Void> updatePoint(Long pointTypeId, PointRequest request) {
		return pointClient.updatePoint(pointTypeId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deletePoint(Long pointTypeId) {
		return pointClient.deletePoint(pointTypeId).getBody();
	}
}
