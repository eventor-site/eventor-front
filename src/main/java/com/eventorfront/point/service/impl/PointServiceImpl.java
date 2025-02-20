package com.eventorfront.point.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
	public Page<GetPointResponse> getPoints(Pageable pageable) {
		return pointClient.getPoints(pageable).getBody();
	}

	@Override
	public GetPointResponse getPoint(Long pointId) {
		return pointClient.getPoint(pointId).getBody();
	}

	@Override
	public void createPoint(PointRequest request) {
		pointClient.createPoint(request);
	}

	@Override
	public void updatePoint(Long pointTypeId, PointRequest request) {
		pointClient.updatePoint(pointTypeId, request);
	}

	@Override
	public void deletePoint(Long pointTypeId) {
		pointClient.deletePoint(pointTypeId);
	}
}
