package com.eventorfront.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.bookmark.service.BookmarkService;
import com.eventorfront.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final PostService postService;
	private final BookmarkService bookmarkService;

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	@GetMapping("/main")
	public String mainPage(Model model) {
		model.addAttribute("hotPosts", postService.getHotEventPosts());
		model.addAttribute("latestPosts", postService.getLatestEventPosts());
		model.addAttribute("recommendedPosts", postService.getRecommendationEventPosts());
		model.addAttribute("bookmarks", bookmarkService.getBookmarksByUserId());
		return "page/main";
	}
}
