package com.eventorfront.comment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {
	private final CommentService commentService;
	private final AuthService authService;

	@GetMapping
	public String getComments(@PathVariable Long postId, Model model) {
		model.addAttribute("comments", commentService.getCommentsByPostId(postId));
		return "comment/list";
	}

	@PostMapping
	public String createComment(@PathVariable Long postId, @ModelAttribute CreateCommentRequest request) {
		commentService.createComment(postId, request);
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/{commentId}")
	public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@ModelAttribute UpdateCommentRequest request) {
		commentService.updateComment(postId, commentId, request);
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/{commentId}/recommend")
	public String recommendComment(@PathVariable Long postId, @PathVariable Long commentId,
		HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			commentService.recommendComment(postId, commentId);
			return "redirect:/posts/" + postId;
		} else {
			return "redirect:/auth/login";
		}
	}

	@PutMapping("/{commentId}/disrecommend")
	public String disrecommendComment(@PathVariable Long postId, @PathVariable Long commentId,
		HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			commentService.disrecommendComment(postId, commentId);
			return "redirect:/posts/" + postId;
		} else {
			return "redirect:/auth/login";
		}
	}

	@DeleteMapping("/{commentId}")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		commentService.deleteComment(postId, commentId);
		return "redirect:/posts/" + postId;
	}
}