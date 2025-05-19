package com.eventorfront.search.service;

import java.util.List;

import com.eventorfront.global.dto.ApiResponse;

public interface SearchService {

	List<String> getTopKeywords();

	ApiResponse<List<String>> getKeywords();

	ApiResponse<Void> deleteKeyword(String keyword);

}
