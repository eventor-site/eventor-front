package com.eventorfront.statistic.dto.response;

import java.time.LocalDate;

public record GetStatistic(
	LocalDate date,
	Long visitorCount,
	Long visitedCount,
	Long loginCount,
	Long signupCount
) {
}
