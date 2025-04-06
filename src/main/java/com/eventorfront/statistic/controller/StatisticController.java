package com.eventorfront.statistic.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.statistic.dto.response.GetStatistic;
import com.eventorfront.statistic.service.StatisticService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/statistics")
public class StatisticController {
	private final StatisticService statisticService;

	@AuthorizeRole("admin")
	@GetMapping
	public String getStatusTypes(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetStatistic> statistics = statisticService.getStatistics(pageable).getData();
		model.addAttribute("objects", statistics);
		PagingModel.pagingProcessing(pageable, model, statistics, "/statistics", 10);
		return "statistic/list";
	}

}
