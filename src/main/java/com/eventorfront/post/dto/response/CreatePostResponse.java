package com.eventorfront.post.dto.response;

import lombok.Builder;

@Builder
public record CreatePostResponse(
	Long postId
) {
}
