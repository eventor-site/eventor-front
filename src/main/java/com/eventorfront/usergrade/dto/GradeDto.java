package com.eventorfront.usergrade.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record GradeDto(
	Long gradeId,
	String name,
	BigDecimal minAmount,
	BigDecimal maxAmount,
	BigDecimal pointRate) {
}
