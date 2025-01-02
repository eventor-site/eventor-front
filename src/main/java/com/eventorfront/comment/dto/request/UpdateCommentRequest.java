package com.eventorfront.comment.dto.request;

import lombok.Builder;

@Builder
public record UpdateCommentRequest(
	String content) {
}
