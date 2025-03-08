package com.eventorfront.auth.dto.response;

import lombok.Builder;

@Builder
public record OauthRedirectUrlResponse(
	String oauthRedirectUrl
) {
}
