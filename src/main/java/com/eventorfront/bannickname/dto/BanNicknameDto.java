package com.eventorfront.bannickname.dto;

import lombok.Builder;

@Builder
public record BanNicknameDto(
	Long banNicknameId,
	String nickname) {
}
