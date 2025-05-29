package com.eventorfront.tour.dto.response;

import lombok.Builder;

@Builder
public record TourApiResponse(
	String addr1,
	String contentid,
	String contentTypeId,
	String title,
	String dist,
	String tel,
	String firstimage
) {
}
