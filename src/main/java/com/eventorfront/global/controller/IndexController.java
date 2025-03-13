package com.eventorfront.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.bookmark.service.BookmarkService;
import com.eventorfront.post.service.PostService;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@lombok.extern.slf4j.Slf4j
@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {
	private final PostService postService;
	private final BookmarkService bookmarkService;

	// @GetMapping("/")
	// public String indexPage() {
	// 	return "index";
	// }

	@GetMapping("/")
	public String mainPage(Model model) {
		log.info("Main Page 실행 ");
		model.addAttribute("hotPosts", postService.getHotEventPosts().getData());
		model.addAttribute("latestPosts", postService.getLatestEventPosts().getData());
		model.addAttribute("deadlinePosts", postService.getDeadlineEventPosts().getData());
		model.addAttribute("recommendedPosts", postService.getRecommendationEventPosts().getData());
		model.addAttribute("trendingPosts", postService.getTrendingEventPosts().getData());
		model.addAttribute("bookmarks", bookmarkService.getBookmarksByUserId().getData());
		return "page/main";
	}
}
