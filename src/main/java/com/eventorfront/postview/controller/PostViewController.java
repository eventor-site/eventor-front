package com.eventorfront.postview.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.postview.service.PostVIewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostViewController {
	private final PostVIewService postViewService;

	@GetMapping("/users/me/postViews")
	public String getPostViewsByViewerId(
		@CookieValue(value = "uuid", required = false) String uuid,
		@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		ApiResponse<Page<GetMainPostResponse>> response = postViewService.getPostViewsByViewerId(uuid, pageable);
		Page<GetMainPostResponse> postViews = response.getData();
		model.addAttribute("objects", postViews);
		PagingModel.pagingProcessing(pageable, model, postViews, "/users/me/postViews", 10);
		return "postView/list :: #postViews";
	}

	@DeleteMapping("/postViews/{postViewId}")
	public String deletePostView(@PathVariable Long postViewId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", postViewService.deletePostView(postViewId).getMessage());
		return "redirect:/users/me/postViews";
	}
}
