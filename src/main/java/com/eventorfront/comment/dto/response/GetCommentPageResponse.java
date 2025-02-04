package com.eventorfront.comment.dto.response;

import lombok.Builder;

@Builder
public record GetCommentPageResponse(
	Long page) {
}
