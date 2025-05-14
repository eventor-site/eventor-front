package com.eventorfront.search.service.impl;

import java.util.List;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.search.client.SearchClient;
import com.eventorfront.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final SearchClient searchClient;

	@Override
	@Scheduled(cron = "0 */5 * * * *") // 매 5분마다
	@CachePut("topKeywords")
	public ApiResponse<List<String>> renewTopKeywords() {
		return searchClient.getTopKeywords().getBody();
	}

	@Override
	@Cacheable("topKeywords")
	public ApiResponse<List<String>> getTopKeywords() {
		return searchClient.getTopKeywords().getBody();
	}

}
