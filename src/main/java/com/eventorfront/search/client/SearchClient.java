package com.eventorfront.search.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "search-client", url = "${feignClient.url}")
public interface SearchClient {

	@GetMapping("/back/searches/keywords")
	ResponseEntity<ApiResponse<List<String>>> getKeywords();

	@DeleteMapping("/back/searches/keywords")
	ResponseEntity<ApiResponse<Void>> deleteKeyword(@RequestParam String keyword);

}
