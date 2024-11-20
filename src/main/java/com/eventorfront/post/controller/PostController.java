package com.eventorfront.post.controller;

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

import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;

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

	@GetMapping("/all")
	public String getPosts(Model model) {
		model.addAttribute("categoryName", "전체");
		model.addAttribute("posts", postService.getPosts());
		return "post/list";
	}

	@GetMapping
	public String getPostsByCategoryName(Model model, @RequestParam String categoryName) {
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("posts", postService.getPostsByCategoryName(categoryName));
		return "post/list";
	}

	@GetMapping("/{postId}")
	public String getPost(Model model, @PathVariable Long postId) {
		model.addAttribute("post", postService.getPost(postId));
		return "post/get";
	}

	@PostMapping
	public String createPost(@ModelAttribute CreatePostRequest request) {
		return "redirect:/posts/" + postService.createPost(request).postId();
	}

	@PutMapping("/{postId}")
	public String updatePost(@PathVariable Long postId, @ModelAttribute UpdatePostRequest request) {
		postService.updatePost(postId, request);
		return "redirect:/posts/" + postId;
	}

	@DeleteMapping("/{postId}")
	public String deletePost(@PathVariable Long postId) {
		postService.deletePost(postId);
		return "redirect:/posts/all";
	}
}