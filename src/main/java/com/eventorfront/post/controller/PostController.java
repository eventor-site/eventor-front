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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.category.service.CategoryService;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.comment.service.CommentService;
import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.global.exception.ForbiddenException;
import com.eventorfront.global.util.CalendarUtils;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.global.util.PermissionUtils;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
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
	private final CategoryService categoryService;

	@GetMapping("/createForm")
	public String createPostForm(@RequestParam String categoryName, Model model) {
		List<String> roles = userService.meRoles().getData();

		// 비회원일 경우 차단
		if (roles.isEmpty()) {
			throw new ForbiddenException();
		}
		model.addAttribute("categoryName", categoryName);

		// 기존 임시 저장 게시물이 있는지 확인
		model.addAttribute("tempPost", postService.getTempPost().getData());

		if (categoryName.equals("자유")) {
			model.addAttribute("categoryType", "자유");
			return "post/createForm";
		} else if (categoryName.equals("핫딜")) {
			model.addAttribute("categoryType", "핫딜");
			return "post/hotDeal/createForm";
		} else if (PermissionUtils.bestFoodCategories.contains(categoryName)) {
			model.addAttribute("categoryType", "맛집");
			model.addAttribute("categories", categoryService.getCategories("맛집").getData());
			return "post/eatery/createForm";
		} else if (!roles.contains("admin")) {
			throw new ForbiddenException();
		} else if (categoryName.equals("공지")) {
			model.addAttribute("categoryType", "공지");
			return "post/createForm";
		} else {
			model.addAttribute("startTime", CalendarUtils.getDate());
			model.addAttribute("endTime", CalendarUtils.getPlusDate(1));
			model.addAttribute("categoryType", "이벤트");
			model.addAttribute("categories", categoryService.getCategories("이벤트").getData());
			return "post/event/createForm";
		}

	}

	@GetMapping("/{postId}/updateForm")
	public String updatePostForm(@PathVariable Long postId, Model model) {
		if (!postService.isAuthorizedToEdit(postId).getData()) {
			throw new ForbiddenException();
		}

		GetPostResponse post = postService.getPost(postId).getData();
		String categoryName = post.categoryName();
		model.addAttribute("post", post);

		if (categoryName.equals("자유")) {
			model.addAttribute("categoryType", "자유");
			return "post/updateForm";
		} else if (categoryName.equals("핫딜")) {
			model.addAttribute("categoryType", "핫딜");
			return "post/hotDeal/updateForm";
		} else if (PermissionUtils.bestFoodCategories.contains(categoryName)) {
			model.addAttribute("categoryType", "맛집");
			model.addAttribute("categories", categoryService.getCategories("맛집").getData());
			return "post/eatery/updateForm";
		} else if (categoryName.equals("공지")) {
			model.addAttribute("categoryType", "공지");
			return "post/updateForm";
		} else {
			model.addAttribute("categoryType", "이벤트");
			model.addAttribute("categories", categoryService.getCategories("이벤트").getData());
			return "post/event/updateForm";
		}

	}

	@GetMapping("/search")
	public String searchPosts(@PageableDefault(page = 1, size = 10) Pageable pageable, @RequestParam String keyword,
		Model model) {
		Page<SearchPostsResponse> posts = postService.searchPosts(pageable, keyword).getData();
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/search?keyword=" + encodedKeyword, 10);
		return "post/search";
	}

	@GetMapping("/all")
	public String getPosts(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostSimpleResponse> posts = postService.getPosts(pageable).getData();
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/all", 10);
		return "post/all";
	}

	@GetMapping
	public String getPostsByCategoryName(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model,
		@RequestParam String categoryName) {
		List<String> roles = userService.meRoles().getData();
		model.addAttribute("categoryName", categoryName);
		model.addAttribute("isAuthorized",
			roles.contains("admin") || (roles.contains("member") && PermissionUtils.memberCategories.contains(
				categoryName))
		);

		Page<GetPostsByCategoryNameResponse> posts = postService.getPostsByCategoryName(pageable, categoryName)
			.getData();
		String encodedCategoryName = URLEncoder.encode(categoryName, StandardCharsets.UTF_8);
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts?categoryName=" + encodedCategoryName, 10);

		if (categoryName.equals("공지")) {
			return "post/noticeList";
		} else {
			model.addAttribute("hotPosts", postService.getHotPostsByCategoryName(categoryName).getData());
			return "post/list";
		}
	}

	@GetMapping("/me")
	public String getPostsByUserId(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostSimpleResponse> posts = postService.getPostsByUserId(pageable).getData();
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/me", 10);
		return "post/me";
	}

	@GetMapping("/{postId}")
	public String getPost(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model,
		@PathVariable Long postId) {
		Page<GetCommentResponse> comments = commentService.getCommentsByPostId(pageable, postId).getData();
		model.addAttribute("post", postService.getPost(postId).getData());
		model.addAttribute("objects", comments);
		PagingModel.pagingProcessing(pageable, model, comments, "/posts/" + postId, 10);
		return "post/get";
	}

	@PostMapping
	public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@ModelAttribute CreatePostRequest request,
		@RequestParam(defaultValue = "false") boolean isTemp) {
		return ResponseEntity.ok(postService.createPost(request, isTemp));
	}

	@PutMapping("/{postId}")
	public ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long postId,
		@ModelAttribute UpdatePostRequest request,
		@RequestParam(defaultValue = "false") boolean isTemp) {
		return ResponseEntity.ok(postService.updatePost(postId, request, isTemp));
	}

	@PutMapping("/{postId}/recommend")
	public ResponseEntity<String> recommendPost(@PathVariable Long postId, HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return ResponseEntity.ok(postService.recommendPost(postId).getMessage());
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@PutMapping("/{postId}/disrecommend")
	public ResponseEntity<String> disrecommendPost(@PathVariable Long postId, HttpServletRequest request) {
		if (authService.hasTokensInCookie(request)) {
			return ResponseEntity.ok(postService.disrecommendPost(postId).getMessage());
		} else {
			return ResponseEntity.ok().body("로그인 후 다시 시도하세요.");
		}
	}

	@DeleteMapping("/{postId}")
	public String deletePost(@PathVariable Long postId, @RequestParam String categoryName,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", postService.deletePost(postId).getMessage());
		String encodedCategoryName = URLEncoder.encode(categoryName, StandardCharsets.UTF_8);
		return "redirect:/posts?categoryName=" + encodedCategoryName;
	}

	@DeleteMapping("/temp")
	public ResponseEntity<Void> deleteTempPost() {
		postService.deleteTempPost();
		return ResponseEntity.ok().build();
	}
}