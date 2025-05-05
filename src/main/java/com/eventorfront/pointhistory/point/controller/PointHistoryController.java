package com.eventorfront.pointhistory.point.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.pointhistory.point.dto.response.GetUserPointTotalResponse;
import com.eventorfront.pointhistory.point.service.PointHistoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/pointHistories")
public class PointHistoryController {
	private final PointHistoryService pointHistoryService;

	@AuthorizeRole("admin")
	@GetMapping
	public String getUserPointTotalsByPeriod(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
		@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {

		if (startDate == null || endDate == null) {
			model.addAttribute("startDate", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			model.addAttribute("endDate",
				LocalDate.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		} else {
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);

			LocalDateTime convertStartDate = startDate.atStartOfDay();
			LocalDateTime convertEndDate = endDate.atTime(LocalTime.MAX);

			Page<GetUserPointTotalResponse> points = pointHistoryService.getUserPointTotalsByPeriod(
				convertStartDate, convertEndDate, pageable).getData();
			model.addAttribute("objects", points);
			PagingModel.pagingProcessing(pageable, model, points, "/pointHistories", 10);

		}
		return "user/admin/pointStatistic";
	}
}