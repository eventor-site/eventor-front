package com.eventorfront.user.dto.request;

import lombok.Builder;

@Builder
public record RecoverOauthRequest(
	String oauthId,
	String email
) {
}
