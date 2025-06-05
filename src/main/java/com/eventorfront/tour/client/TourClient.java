package com.eventorfront.tour.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.tour.dto.response.GetTourResponse;
import com.eventorfront.tour.dto.response.SearchFestivalResponse;
import com.eventorfront.tour.dto.response.SearchTourResponse;

@FeignClient(name = "tour-client", url = "${feignClient.url}/back/tours")
public interface TourClient {

	@GetMapping("/search")
	ResponseEntity<ApiResponse<SearchTourResponse>> searchTour(
		@RequestParam String address, @RequestParam String radius);

	@GetMapping("/{contentId}")
	ResponseEntity<ApiResponse<GetTourResponse>> getTour(
		@PathVariable String contentId, @RequestParam String contentTypeId);

	@GetMapping("/festival2")
	ResponseEntity<ApiResponse<List<SearchFestivalResponse>>> searchFestival2();
}
