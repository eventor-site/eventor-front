package com.eventorfront.comment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.service.CommentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	private final AuthService authService;

	@GetMapping("/users/me/comments")
	public String getCommentsByUserId(Model model) {
		model.addAttribute("comments", commentService.getCommentsByUserId());
		return "comment/me";
	}

	@PostMapping("/posts/{postId}/comments")
	public String createComment(@PathVariable Long postId, @ModelAttribute CreateCommentRequest request) {
		commentService.createComment(postId, request);
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/posts/{postId}/comments/{commentId}")
	public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@ModelAttribute UpdateCommentRequest request) {
		commentService.updateComment(postId, commentId, request);
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/posts/{postId}/comments/{commentId}/recommend")
	public ResponseEntity<String> recommendComment(@PathVariable Long postId, @PathVariable Long commentId,
		HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return commentService.recommendComment(postId, commentId);
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@PutMapping("/posts/{postId}/comments/{commentId}/disrecommend")
	public ResponseEntity<String> disrecommendComment(@PathVariable Long postId, @PathVariable Long commentId,
		HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return commentService.disrecommendComment(postId, commentId);
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
		commentService.deleteComment(postId, commentId);
		return "redirect:/posts/" + postId;
	}
}