package com.eventorfront.tour.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.tour.client.TourClient;
import com.eventorfront.tour.dto.response.GetOngoingFestivalResponse;
import com.eventorfront.tour.dto.response.GetTourResponse;
import com.eventorfront.tour.dto.response.SearchFestivalResponse;
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

	@Override
	public ApiResponse<List<SearchFestivalResponse>> searchFestival2() {
		return tourClient.searchFestival2().getBody();
	}

	@Override
	public ApiResponse<List<GetOngoingFestivalResponse>> getOngoingFestivals() {
		return tourClient.getOngoingFestivals().getBody();
	}
}
