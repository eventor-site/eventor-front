package com.eventorfront.pointhistory.point.service.impl;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.pointhistory.point.client.PointHistoryClient;
import com.eventorfront.pointhistory.point.dto.response.GetUserPointTotalResponse;
import com.eventorfront.pointhistory.point.service.PointHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {
	private final PointHistoryClient pointClient;

	@Override
	public ApiResponse<Page<GetUserPointTotalResponse>> getUserPointTotalsByPeriod(LocalDateTime startDate,
		LocalDateTime endDate, Pageable pageable) {
		return pointClient.getUserPointTotalsByPeriod(startDate, endDate, pageable).getBody();
	}
}
