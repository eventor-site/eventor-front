package com.eventorfront.search.service.impl;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.search.client.SearchClient;
import com.eventorfront.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final RedisTemplate<String, Object> keywordRedisTemplate;
	private final SearchClient searchClient;

	@Override
	public List<String> getTopKeywords() {
		return keywordRedisTemplate.opsForZSet()
			.reverseRange("search_keywords:score", 0, 9)
			.stream()
			.map(String::valueOf)
			.toList();
	}

	@Override
	public ApiResponse<List<String>> getKeywords() {
		return searchClient.getKeywords().getBody();
	}

	@Override
	public ApiResponse<Void> deleteKeyword(String keyword) {
		return searchClient.deleteKeyword(keyword).getBody();
	}

}
