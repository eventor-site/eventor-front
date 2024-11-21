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
		model.addAttribute("latestPosts", postService.getPostsByCategoryName("전체"));
		model.addAttribute("hotPosts", postService.getPostsByCategoryName("전체"));
		model.addAttribute("recommendedPosts", postService.getPostsByCategoryName("전체"));
		return "page/main";
	}

	@GetMapping("/users/admin")
	public String adminPage() {
		return "admin";
	}

	// @GetMapping("/")
	// public String indexPage() {
	// 	return "redirect:/main";
	// }

	@GetMapping("/api/users/me")
	public String userPage() {
		return "user/user-account";
	}

	@GetMapping("/template")
	public String showTemplate() {
		return "menu-template";
	}
}
