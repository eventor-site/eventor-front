package com.eventorfront.pointhistory.point.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.pointhistory.point.dto.response.GetUserPointTotalResponse;

public interface PointHistoryService {
	ApiResponse<Page<GetUserPointTotalResponse>> getUserPointTotalsByPeriod(LocalDateTime startDate,
		LocalDateTime endDate, Pageable pageable);

}
