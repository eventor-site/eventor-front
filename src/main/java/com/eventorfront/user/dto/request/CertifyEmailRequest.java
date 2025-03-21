package com.eventorfront.user.dto.request;

import lombok.Builder;

@Builder
public record CertifyEmailRequest(
	String email,
	String type,
	String certifyCode
) {
}
