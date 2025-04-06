package com.eventorfront.statistic.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.statistic.client.StatisticClient;
import com.eventorfront.statistic.dto.response.GetStatistic;
import com.eventorfront.statistic.service.StatisticService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
	private final StatisticClient statisticClient;

	@Override
	public ApiResponse<Page<GetStatistic>> getStatistics(Pageable pageable) {
		return statisticClient.getStatistics(pageable).getBody();
	}

	@Override
	public void saveVisitor(String uuid) {
		statisticClient.saveVisitor(uuid);
	}
}
