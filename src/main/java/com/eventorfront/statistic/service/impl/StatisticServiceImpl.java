package com.eventorfront.statistic.service.impl;

import org.springframework.stereotype.Service;

import com.eventorfront.statistic.client.StatisticClient;
import com.eventorfront.statistic.service.StatisticService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticServiceImpl implements StatisticService {
	private final StatisticClient statisticClient;

	@Override
	public void saveVisitor(String uuid) {
		statisticClient.saveVisitor(uuid);
	}
}
