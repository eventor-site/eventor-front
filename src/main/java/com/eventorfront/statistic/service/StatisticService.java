package com.eventorfront.statistic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.statistic.dto.response.GetStatistic;

public interface StatisticService {

	ApiResponse<Page<GetStatistic>> getStatistics(Pageable pageable);

	void saveVisitor(String uuid);
}
