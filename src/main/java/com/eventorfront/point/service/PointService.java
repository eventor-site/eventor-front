package com.eventorfront.point.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.point.dto.request.PointRequest;
import com.eventorfront.point.dto.response.GetPointResponse;

public interface PointService {

	ApiResponse<Page<GetPointResponse>> getPoints(Pageable pageable);

	ApiResponse<GetPointResponse> getPoint(Long pointId);

	ApiResponse<Void> createPoint(PointRequest request);

	ApiResponse<Void> updatePoint(Long pointId, PointRequest request);

	ApiResponse<Void> deletePoint(Long pointId);
}
