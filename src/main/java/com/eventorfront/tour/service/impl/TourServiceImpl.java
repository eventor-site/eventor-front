package com.eventorfront.tour.service.impl;

import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.tour.client.TourClient;
import com.eventorfront.tour.dto.response.GetTourResponse;
import com.eventorfront.tour.dto.response.SearchTourResponse;
import com.eventorfront.tour.service.TourService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService {
	private final TourClient tourClient;

	@Override
	public ApiResponse<SearchTourResponse> searchTour(String address, String radius) {
		return tourClient.searchTour(address, radius).getBody();
	}

	@Override
	public ApiResponse<GetTourResponse> getTour(String contentId, String contentTypeId) {
		return tourClient.getTour(contentId, contentTypeId).getBody();
	}
}
