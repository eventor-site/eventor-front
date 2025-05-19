package com.eventorfront.search.service.impl;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.eventorfront.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
	private final RedisTemplate<String, Object> keywordRedisTemplate;

	@Override
	public List<String> getTopKeywords() {
		return keywordRedisTemplate.opsForZSet()
			.reverseRange("search_keywords:score", 0, 9)
			.stream()
			.map(String::valueOf)
			.toList();
	}

	@Override
	public List<String> getKeywords() {
		return keywordRedisTemplate.opsForZSet()
			.reverseRange("search_keywords:score", 0, 99)
			.stream()
			.map(String::valueOf)
			.toList();
	}

	@Override
	public void deleteKeyword(String keyword) {
		keywordRedisTemplate.opsForZSet().remove("search_keywords:total", keyword);
		keywordRedisTemplate.opsForZSet().remove("search_keywords:score", keyword);
		keywordRedisTemplate.opsForHash().delete("search_keywords:last_used", keyword);
	}

}
