package com.eventorfront.pointhistory.point.controller;

import java.time.LocalDateTime;

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
import com.eventorfront.global.util.CalendarUtils;
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
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
		@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {

		if (startTime == null || endTime == null) {
			model.addAttribute("startTime", CalendarUtils.getDate());
			model.addAttribute("endTime", CalendarUtils.getPlusDate(7));
			model.addAttribute("objects", Page.empty());
		} else {
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);

			Page<GetUserPointTotalResponse> points = pointHistoryService.getUserPointTotalsByPeriod(
				startTime, endTime, pageable).getData();
			model.addAttribute("objects", points);
			PagingModel.pagingProcessing(pageable, model, points, "/pointHistories", 10);

		}
		return "user/admin/pointStatistic";
	}
}