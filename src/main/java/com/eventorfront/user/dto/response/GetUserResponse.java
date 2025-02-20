package com.eventorfront.user.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record GetUserResponse(
	String identifier,
	String name,
	String nickname,
	String email,
	String phone,
	LocalDate birth,
	String gender,
	Long point,
	String statusName,
	String gradeName,
	List<String> roles,
	String oauthType,
	LocalDateTime connectTime,
	LocalDateTime createdAt,
	LocalDateTime updatedTime,
	LocalDateTime lastLoginTime
) {
}
