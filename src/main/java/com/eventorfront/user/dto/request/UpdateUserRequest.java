package com.eventorfront.user.dto.request;

import java.time.LocalDate;

import lombok.Builder;

@Builder
public record UpdateUserRequest(
	String name,
	String nickname,
	String email,
	String phone,
	LocalDate birth,
	String gender
) {
}
