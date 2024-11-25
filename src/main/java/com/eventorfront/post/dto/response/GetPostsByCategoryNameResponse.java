package com.eventorfront.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record GetPostsByCategoryNameResponse(
	String status,
	String message,
	LocalDateTime serverTime,
	Boolean isAuthorized,
	List<GetPostSimpleResponse> data
) {
}
