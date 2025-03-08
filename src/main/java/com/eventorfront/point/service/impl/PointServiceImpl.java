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
		return pointClient.getPoints(pageable).getData();
	}

	@Override
	public GetPointResponse getPoint(Long pointId) {
		return pointClient.getPoint(pointId).getData();
	}

	@Override
	public String createPoint(PointRequest request) {
		return pointClient.createPoint(request).getMessage();
	}

	@Override
	public String updatePoint(Long pointTypeId, PointRequest request) {
		return pointClient.updatePoint(pointTypeId, request).getMessage();
	}

	@Override
	public String deletePoint(Long pointTypeId) {
		return pointClient.deletePoint(pointTypeId).getMessage();
	}
}
