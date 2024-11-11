package com.eventorfront.usergrade.dto;

import java.math.BigDecimal;

import lombok.Builder;

@Builder
public record UserGradeDto(
	Long userGradeId,
	String name,
	BigDecimal minAmount,
	BigDecimal maxAmount,
	BigDecimal pointRate) {
}
