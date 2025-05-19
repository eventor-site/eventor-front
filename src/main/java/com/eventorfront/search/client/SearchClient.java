package com.eventorfront.search.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "search-client", url = "${feignClient.url}")
public interface SearchClient {

	@GetMapping("/back/searches/keywords")
	ResponseEntity<ApiResponse<List<String>>> getKeywords();

}
