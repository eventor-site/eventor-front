package com.eventorfront.global.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/robots.txt")
	public ResponseEntity<String> getRobotsTxt() {
		String robotsTxt = "User-agent: *\nAllow: /";
		return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(robotsTxt);
	}

	@GetMapping("/main")
	public String mainPage(Model model) {
		model.addAttribute("hotPosts", postService.getHotEventPosts());
		model.addAttribute("latestPosts", postService.getLatestEventPosts());
		model.addAttribute("recommendedPosts", postService.getRecommendationEventPosts());
		model.addAttribute("trendingPosts", postService.getTrendingEventPosts());
		model.addAttribute("bookmarks", bookmarkService.getBookmarksByUserId());
		return "page/main";
	}
}
