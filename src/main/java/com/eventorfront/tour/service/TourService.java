package com.eventorfront.tour.service;

import java.util.List;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.tour.dto.response.GetTourResponse;
import com.eventorfront.tour.dto.response.SearchFestivalResponse;
import com.eventorfront.tour.dto.response.SearchTourResponse;

public interface TourService {

	ApiResponse<SearchTourResponse> searchTour(String address, String radius);

	ApiResponse<GetTourResponse> getTour(String contentId, String contentTypeId);

	ApiResponse<List<SearchFestivalResponse>> searchFestival2();

}
