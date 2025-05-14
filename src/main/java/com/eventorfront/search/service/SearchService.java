package com.eventorfront.search.service;

import java.util.List;

import com.eventorfront.global.dto.ApiResponse;

public interface SearchService {

	ApiResponse<List<String>> renewTopKeywords();

	ApiResponse<List<String>> getTopKeywords();

}
