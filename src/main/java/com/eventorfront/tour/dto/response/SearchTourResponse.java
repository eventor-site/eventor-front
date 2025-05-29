package com.eventorfront.tour.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record SearchTourResponse(
	List<TourApiResponse> tours,
	List<TourApiResponse> eateries,
	List<TourApiResponse> hotels
) {
}
