package com.eventorfront.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final PostService postService;

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	@GetMapping("/main")
	public String mainPage(Model model) {
		model.addAttribute("hotPosts", postService.getHotEventPosts());
		model.addAttribute("latestPosts", postService.getLatestEventPosts());
		model.addAttribute("recommendedPosts", postService.getRecommendationEventPosts());
		return "page/main";
	}

	// @GetMapping("/")
	// public String indexPage() {
	// 	return "redirect:/main";
	// }

	@GetMapping("/template")
	public String showTemplate() {
		return "menu-template";
	}
}
