package com.eventorfront.comment.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.auth.service.AuthService;
import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.service.CommentService;
import com.eventorfront.global.util.PagingModel;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentController {
	private final CommentService commentService;
	private final AuthService authService;

	@AuthorizeRole("admin")
	@GetMapping("/users/admin/comments")
	public String getComments(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetCommentByUserIdResponse> comments = commentService.getComments(pageable).getData();
		model.addAttribute("objects", comments);
		PagingModel.pagingProcessing(pageable, model, comments, "/users/admin/comments", 10);
		return "comment/admin";
	}

	@GetMapping("/users/me/comments")
	public String getCommentsByUserId(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetCommentByUserIdResponse> comments = commentService.getCommentsByUserId(pageable).getData();
		model.addAttribute("objects", comments);
		PagingModel.pagingProcessing(pageable, model, comments, "/users/me/comments", 10);
		return "comment/me";
	}

	@GetMapping("/posts/{postId}/comments/{commentId}")
	public String getComment(@PathVariable Long postId, @PathVariable Long commentId) {
		Long page = commentService.getComment(postId, commentId).getData().page();
		return "redirect:/posts/" + postId + "?page=" + page + "&size=10" + "#" + commentId;
	}

	@PostMapping("/posts/{postId}/comments")
	public String createComment(@PathVariable Long postId, @ModelAttribute CreateCommentRequest request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", commentService.createComment(postId, request).getMessage());
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/posts/{postId}/comments/{commentId}")
	public String updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@ModelAttribute UpdateCommentRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			commentService.updateComment(postId, commentId, request).getMessage());
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/posts/{postId}/comments/{commentId}/recommend")
	public ResponseEntity<String> recommendComment(@PathVariable Long postId, @PathVariable Long commentId,
		HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return ResponseEntity.ok(commentService.recommendComment(postId, commentId).getMessage());
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@PutMapping("/posts/{postId}/comments/{commentId}/disrecommend")
	public ResponseEntity<String> disrecommendComment(@PathVariable Long postId, @PathVariable Long commentId,
		HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return ResponseEntity.ok(commentService.disrecommendComment(postId, commentId).getMessage());
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", commentService.deleteComment(postId, commentId).getMessage());
		return "redirect:/posts/" + postId;
	}
}