package com.eventorfront.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.bookmark.service.BookmarkService;
import com.eventorfront.post.service.PostService;
import com.eventorfront.tour.service.TourService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	private final PostService postService;
	private final BookmarkService bookmarkService;
	private final AuthService authService;
	private final TourService tourService;

	// @GetMapping("/")
	// public String indexPage() {
	// 	return "index";
	// }

	@GetMapping("/")
	public String mainPage(HttpServletRequest request, Model model) {
		model.addAttribute("isLogin", authService.hasTokensInCookie(request));
		model.addAttribute("hotPosts", postService.getHotEventPosts().getData());
		model.addAttribute("latestPosts", postService.getLatestEventPosts().getData());
		model.addAttribute("deadlinePosts", postService.getDeadlineEventPosts().getData());
		model.addAttribute("recommendedPosts", postService.getRecommendationEventPosts().getData());
		model.addAttribute("trendingPosts", postService.getTrendingEventPosts().getData());
		model.addAttribute("hotDealPosts", postService.getHotDealPosts().getData());
		model.addAttribute("ongoingFestivals", tourService.getOngoingFestivals().getData());
		model.addAttribute("communityPosts", postService.getCommunityPosts().getData());
		model.addAttribute("bookmarks", bookmarkService.getBookmarksByUserId().getData());

		return "page/main";
	}

	@GetMapping("/advertisement/eventModal")
	public String advertisementModal() {
		return "advertisement/eventModal";
	}
}
