package com.eventorfront.search.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@GetMapping("/keywords")
	public String getKeywords(Model model) {
		model.addAttribute("keywords", searchService.getKeywords().getData());
		return "search/keywords";
	}

	@DeleteMapping("/keywords")
	public String deleteKeyword(@RequestParam String keyword, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", searchService.deleteKeyword(keyword).getMessage());
		return "redirect:/searches/keywords";
	}

}