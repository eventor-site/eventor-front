package com.eventorfront.tour.service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.tour.dto.response.GetTourResponse;
import com.eventorfront.tour.dto.response.SearchTourResponse;

public interface TourService {

	ApiResponse<SearchTourResponse> searchTour(String address, String radius);

	ApiResponse<GetTourResponse> getTour(String contentId, String contentTypeId);
}
