package com.eventorfront.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.search.service.SearchService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/searches")
public class SearchController {
	private final SearchService searchService;

	@GetMapping("/topKeywords")
	public String getTopKeywords(Model model) {
		model.addAttribute("topKeywords", searchService.getTopKeywords());
		return "search/topKeywords :: #topKeywords";
	}

	@GetMapping("/topKeywords/sidebar")
	public String getSidebarTopKeywords(Model model) {
		model.addAttribute("topKeywords", searchService.getTopKeywords());
		return "search/sidebarTopKeywords :: #sidebarTopKeywords";
	}

}