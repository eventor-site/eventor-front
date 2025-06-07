package com.eventorfront.statistic.dto.response;

import java.time.LocalDate;

public record GetStatistic(
	LocalDate date,
	Long visitorCount,
	Long mainViewCount,
	Long postViewCount,
	Long loginCount,
	Long signupCount
) {
}
