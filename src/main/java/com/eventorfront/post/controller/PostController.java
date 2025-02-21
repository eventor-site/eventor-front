package com.eventorfront.post.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.comment.service.CommentService;
import com.eventorfront.global.exception.ForbiddenException;
import com.eventorfront.global.util.FormUtils;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.dto.response.GetPostsByCategoryNameResponse;
import com.eventorfront.post.service.PostService;
import com.eventorfront.search.dto.response.SearchPostsResponse;
import com.eventorfront.user.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
	private final PostService postService;
	private final CommentService commentService;
	private final AuthService authService;
	private final UserService userService;

	@GetMapping("/create")
	public String createPostForm(@RequestParam String categoryName, Model model) {
		// List<String> roles = userService.meRoles();

		// // 비회원일 경우 차단, 멤버는 허용된 카테고리만 접근 가능
		// if (roles.isEmpty() || roles.contains("member") && !PermissionUtils.categories.contains(
		// 	categoryName)) {
		// 	throw new ForbiddenException();
		// }

		model.addAttribute("categoryName", categoryName);
		if (categoryName.equals("핫딜")) {
			model.addAttribute("content", FormUtils.HOT_DEAL);
		}

		return "post/create";

	}

	@GetMapping("/{postId}/update")
	public String updatePostForm(@PathVariable Long postId, Model model) {
		if (!postService.isAuthorizedToEdit(postId)) {
			throw new ForbiddenException();
		}
		model.addAttribute("post", postService.getPost(postId));
		return "post/update";
	}

	@GetMapping("/search")
	public String searchPosts(@PageableDefault(page = 1, size = 10) Pageable pageable, @RequestParam String keyword,
		Model model) {
		Page<SearchPostsResponse> posts = postService.searchPosts(pageable, keyword);
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/search?keyword=" + encodedKeyword, 10);
		return "post/search";
	}

	@GetMapping("/all")
	public String getPosts(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostSimpleResponse> posts = postService.getPosts(pageable);
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/all", 10);
		return "post/all";
	}

	@GetMapping
	public String getPostsByCategoryName(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model,
		@RequestParam String categoryName) {
		List<String> roles = userService.meRoles();
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("isAuthorized",
			roles.contains("admin") || (roles.contains("member") && List.of("자유", "핫딜", "맛집").contains(categoryName))
		);

		Page<GetPostsByCategoryNameResponse> posts = postService.getPostsByCategoryName(pageable, categoryName);
		String encodedCategoryName = URLEncoder.encode(categoryName, StandardCharsets.UTF_8);
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts?categoryName=" + encodedCategoryName, 10);

		if (categoryName.equals("공지")) {
			return "post/noticeList";
		} else {
			model.addAttribute("hotPosts", postService.getHotPostsByCategoryName(categoryName));
			return "post/list";
		}
	}

	@GetMapping("/me")
	public String getPostsByUserId(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostSimpleResponse> posts = postService.getPostsByUserId(pageable);
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/me", 10);
		return "post/me";
	}

	@GetMapping("/{postId}")
	public String getPost(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model,
		@PathVariable Long postId) {
		Page<GetCommentResponse> comments = commentService.getCommentsByPostId(pageable, postId);
		model.addAttribute("post", postService.getPost(postId));
		model.addAttribute("objects", comments);
		PagingModel.pagingProcessing(pageable, model, comments, "/posts/" + postId, 10);
		return "post/get";
	}

	@PostMapping
	public String createPost(@ModelAttribute CreatePostRequest request, MultipartFile thumbnail,
		List<MultipartFile> files) {
		return "redirect:/posts/" + postService.createPost(request, thumbnail, files).postId();
	}

	@PutMapping("/{postId}")
	public String updatePost(@PathVariable Long postId, @ModelAttribute UpdatePostRequest request,
		MultipartFile thumbnail, List<MultipartFile> files, @RequestParam(required = false) List<Long> deleteImageIds) {
		postService.updatePost(postId, request, thumbnail, files, deleteImageIds);
		return "redirect:/posts/" + postId;
	}

	@PutMapping("/{postId}/recommend")
	public ResponseEntity<String> recommendPost(@PathVariable Long postId, HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return postService.recommendPost(postId);
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@PutMapping("/{postId}/disrecommend")
	public ResponseEntity<String> disrecommendPost(@PathVariable Long postId, HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return postService.disrecommendPost(postId);
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@DeleteMapping("/{postId}")
	public String deletePost(@PathVariable Long postId, @RequestParam String categoryName) {
		postService.deletePost(postId);
		String encodedCategoryName = URLEncoder.encode(categoryName, StandardCharsets.UTF_8);
		return "redirect:/posts?categoryName=" + encodedCategoryName;
	}
}