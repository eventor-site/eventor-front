package com.eventorfront.usergrade.dto;

import lombok.Builder;

@Builder
public record GradeDto(
	Long gradeId,
	String name,
	Long minAmount,
	Long maxAmount) {
}
