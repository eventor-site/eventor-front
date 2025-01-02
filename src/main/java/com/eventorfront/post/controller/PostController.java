package com.eventorfront.post.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.comment.service.CommentService;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.service.PostService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;
	private final CommentService commentService;
	private final AuthService authService;

	@GetMapping("/create")
	public String createPostForm(@RequestParam String categoryName, Model model) {
		model.addAttribute("categoryName", categoryName);
		return "post/create";
	}

	@GetMapping("/{postId}/update")
	public String updatePostForm(@PathVariable Long postId, Model model) {
		model.addAttribute("post", postService.getPost(postId));
		return "post/update";
	}

	@GetMapping("{postId}/comments/{commentId}/update")
	public String updateCommentForm(@PathVariable Long commentId, Model model, @PathVariable Long postId) {
		model.addAttribute("post", postService.getPost(postId));
		return "post/update";
	}

	@GetMapping("/all")
	public String getPosts(Model model) {
		model.addAttribute("categoryName", "전체");
		model.addAttribute("posts", postService.getPosts());
		return "post/all";
	}

	@GetMapping
	public String getPostsByCategoryName(Model model, @RequestParam String categoryName) {
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("response", postService.getPostsByCategoryName(categoryName));
		return "post/list";
	}

	@GetMapping("/{postId}")
	public String getPost(Model model, @PathVariable Long postId) {
		model.addAttribute("post", postService.getPost(postId));
		model.addAttribute("comments", commentService.getCommentsByPostId(postId));
		return "post/get";
	}

	@PostMapping
	public String createPost(@ModelAttribute CreatePostRequest request,
		@RequestParam("files") List<MultipartFile> files) {
		return "redirect:/posts/" + postService.createPost(request, files).postId();
	}

	@PutMapping("/{postId}")
	public String updatePost(@PathVariable Long postId, @ModelAttribute UpdatePostRequest request) {
		postService.updatePost(postId, request);
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/{postId}/recommend")
	public String recommendPost(@PathVariable Long postId, HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			postService.recommendPost(postId);
			return "redirect:/posts/" + postId;
		} else {
			return "redirect:/auth/login";
		}
	}

	@PutMapping("/{postId}/disrecommendPost")
	public String disrecommendPost(@PathVariable Long postId, HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			postService.disrecommendPost(postId);
			return "redirect:/posts/" + postId;
		} else {
			return "redirect:/auth/login";
		}
	}

	@DeleteMapping("/{postId}")
	public String deletePost(@PathVariable Long postId) {
		postService.deletePost(postId);
		return "redirect:/posts/all";
	}
}