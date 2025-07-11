package com.eventorfront.post.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.auth.service.AuthService;
import com.eventorfront.category.service.CategoryService;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.comment.service.CommentService;
import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.global.exception.ForbiddenException;
import com.eventorfront.global.util.CalendarUtils;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.image.dto.response.GetImageResponse;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetFixedPostResponse;
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
		} else if (categoryService.getCategoryNames("맛집").getData().contains(categoryName)) {
			model.addAttribute("categoryType", "맛집");
			model.addAttribute("categories", categoryService.getCategoryNames("맛집").getData());
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
			model.addAttribute("categories", categoryService.getCategoryNames("이벤트").getData());
			return "post/event/createForm";
		}

	}

	@GetMapping("/{postId}/updateForm")
	public String updatePostForm(@PathVariable Long postId, Model model) {
		if (!postService.isAuthorizedToEdit(postId).getData()) {
			throw new ForbiddenException();
		}

		GetPostResponse post = postService.getPost(null, postId).getData();
		String categoryName = post.categoryName();
		model.addAttribute("post", post);

		List<String> eateries = categoryService.getCategoryNames("맛집").getData();

		if (categoryName.equals("자유")) {
			model.addAttribute("categoryType", "자유");
			return "post/updateForm";
		} else if (categoryName.equals("핫딜")) {
			model.addAttribute("categoryType", "핫딜");
			return "post/hotDeal/updateForm";
		} else if (eateries.contains(categoryName)) {
			model.addAttribute("categoryType", "맛집");
			model.addAttribute("categories", eateries);
			return "post/eatery/updateForm";
		} else if (categoryName.equals("공지")) {
			model.addAttribute("categoryType", "공지");
			return "post/updateForm";
		} else {
			model.addAttribute("categoryType", "이벤트");
			model.addAttribute("categories", categoryService.getCategoryNames("이벤트").getData());
			return "post/event/updateForm";
		}

	}

	@GetMapping("/search")
	public String searchPosts(@PageableDefault(page = 1, size = 10, sort = {
			"createdAt"}, direction = Sort.Direction.DESC) Pageable defaultPageable,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "DESC") String direction,
		@RequestParam(defaultValue = "") String keyword,
		@RequestParam(defaultValue = "") String categoryName,
		@RequestParam(defaultValue = "") String eventStatusName,
		@RequestParam(defaultValue = "") String endType, Model model) {
		List<String> roles = userService.meRoles().getData();

		if (categoryName != null && categoryName.isEmpty()) {
			categoryName = null;
		}

		List<String> memberCategories = categoryService.getCategoryNames("맛집").getData();
		memberCategories.addAll(List.of("맛집", "자유"));

		categoryService.getCategoryNames("맛집").getData().addAll(List.of("맛집", "자유"));

		model.addAttribute("categoryName", categoryName);
		model.addAttribute("keyword", keyword);
		model.addAttribute("isAuthorized",
			roles.contains("admin") || (roles.contains("member") && memberCategories.contains(
				categoryName))
		);

		Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
		Sort sort = Sort.by(sortDirection, sortBy);
		Pageable pageable = PageRequest.of(defaultPageable.getPageNumber(), defaultPageable.getPageSize(), sort);

		model.addAttribute("sortBy", sortBy);
		model.addAttribute("direction", direction);

		Page<SearchPostsResponse> posts = postService.searchPosts(pageable, keyword, categoryName, eventStatusName,
			endType).getData();

		String encodedCategoryName =
			categoryName != null ? URLEncoder.encode(categoryName, StandardCharsets.UTF_8) : "";
		String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
		model.addAttribute("objects", posts);

		boolean isEvent =
			categoryName != null && categoryService.getCategoryNames("이벤트").getData().contains(categoryName);
		model.addAttribute("isEvent", isEvent);

		if (isEvent) {
			model.addAttribute("eventStatusName", eventStatusName);
			model.addAttribute("endType", endType);
			PagingModel.pagingProcessing(pageable, model, posts,
				"/posts/search?categoryName=" + encodedCategoryName + "&direction=" + direction + "&sortBy=" + sortBy
					+ "&keyword=" + encodedKeyword + "&eventStatusName=" + eventStatusName + "&endType=" + endType, 10);
		} else {
			PagingModel.pagingProcessing(pageable, model, posts,
				"/posts/search?categoryName=" + encodedCategoryName + "&direction=" + direction + "&sortBy=" + sortBy
					+ "&keyword=" + encodedKeyword, 10);
		}
		return "post/search";
	}

	@AuthorizeRole("admin")
	@GetMapping("/all")
	public String getPosts(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostSimpleResponse> posts = postService.getPosts(pageable).getData();
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/all", 10);
		return "post/all";
	}

	@AuthorizeRole("admin")
	@GetMapping("/event/special/notice")
	public String getSpecialNoticeEventPosts(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostSimpleResponse> posts = postService.getSpecialNoticeEventPosts(pageable).getData();
		model.addAttribute("objects", posts);
		PagingModel.pagingProcessing(pageable, model, posts, "/posts/event/special/notice", 10);
		return "post/all";
	}

	@GetMapping
	public String getPostsByCategoryName(
		@PageableDefault(page = 1, size = 10, sort = {
			"createdAt"}, direction = Sort.Direction.DESC) Pageable defaultPageable, Model model,
		@RequestParam(defaultValue = "createdAt") String sortBy,
		@RequestParam(defaultValue = "DESC") String direction,
		@RequestParam(defaultValue = "") String categoryName,
		@RequestParam(defaultValue = "") String eventStatusName,
		@RequestParam(defaultValue = "") String endType) {
		List<String> roles = userService.meRoles().getData();
		List<String> memberCategories = categoryService.getCategoryNames("맛집").getData();
		memberCategories.addAll(List.of("맛집", "자유"));

		model.addAttribute("categoryName", categoryName);
		model.addAttribute("isAuthorized",
			roles.contains("admin") || (roles.contains("member") && memberCategories.contains(
				categoryName))
		);

		Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
		Sort sort = Sort.by(sortDirection, sortBy);
		Pageable pageable = PageRequest.of(defaultPageable.getPageNumber(), defaultPageable.getPageSize(), sort);

		List<GetFixedPostResponse> fixedPosts = postService.getFixedPostsByCategoryName(categoryName).getData();
		Page<GetPostsByCategoryNameResponse> posts = postService.getPostsByCategoryName(pageable, categoryName,
			eventStatusName, endType).getData();
		String encodedCategoryName = URLEncoder.encode(categoryName, StandardCharsets.UTF_8);
		model.addAttribute("fixedPosts", fixedPosts);
		model.addAttribute("objects", posts);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("direction", direction);

		if (categoryName.equals("공지")) {
			PagingModel.pagingProcessing(pageable, model, posts,
				"/posts?categoryName=" + encodedCategoryName + "&direction=" + direction + "&sortBy=" + sortBy, 10);
			return "post/notice/list";
		} else {
			model.addAttribute("hotPosts", postService.getHotPostsByCategoryName(categoryName).getData());

			if (categoryService.getCategoryNames("이벤트").getData().contains(categoryName)) {
				model.addAttribute("eventStatusName", eventStatusName);
				model.addAttribute("endType", endType);
				PagingModel.pagingProcessing(pageable, model, posts,
					"/posts?categoryName=" + encodedCategoryName + "&direction=" + direction + "&sortBy=" + sortBy
						+ "&eventStatusName=" + eventStatusName + "&endType=" + endType, 10);
				return "post/event/list";
			} else {
				PagingModel.pagingProcessing(pageable, model, posts,
					"/posts?categoryName=" + encodedCategoryName + "&direction=" + direction + "&sortBy=" + sortBy, 10);
				return "post/list";
			}
		}
	}

	@GetMapping("/{postId}")
	public String getPost(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model,
		@CookieValue(value = "uuid", required = false) String uuid, @PathVariable Long postId) {
		Page<GetCommentResponse> comments = commentService.getCommentsByPostId(pageable, postId).getData();
		GetPostResponse post = postService.getPost(uuid, postId).getData();

		String thumbnailUrl = post.images().stream()
			.filter(GetImageResponse::isThumbnail)
			.map(GetImageResponse::url)
			.findFirst()
			.orElse("https://www.eventor.store/postimage/common/share-logo.png");

		model.addAttribute("thumbnailUrl", thumbnailUrl);
		model.addAttribute("categoryName", post.categoryName());
		model.addAttribute("post", post);
		model.addAttribute("objects", comments);
		model.addAttribute("isEvent", categoryService.getCategoryNames("이벤트").getData().contains(post.categoryName()));
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

	@PutMapping("/{postId}/finish")
	public String finishEventPost(@PathVariable Long postId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", postService.finishEventPost(postId).getMessage());
		return "redirect:/posts/" + postId;
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

	@PutMapping("/{postId}/isFixed")
	public String updatePostIsFixed(@PathVariable Long postId, @RequestParam Boolean isFixed,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", postService.updatePostIsFixed(postId, isFixed).getMessage());
		return "redirect:/posts/" + postId;
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

	@AuthorizeRole("admin")
	@GetMapping("/statistic/users/admin")
	public String getEventPostCountByAdmin(
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
		Model model) {

		if (startTime == null || endTime == null) {
			model.addAttribute("startTime", CalendarUtils.getDate());
			model.addAttribute("endTime", CalendarUtils.getPlusDate(1));
		} else {
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("eventPostStats", postService.getEventPostCountByAdmin(startTime, endTime).getData());
		}

		return "user/admin/statistic";
	}

	@AuthorizeRole("admin")
	@GetMapping("/event/deleteForm")
	public String deleteEventPostsPage() {
		return "user/admin/eventDelete";
	}

	@DeleteMapping("/event")
	public String deleteEventPostsByTitleContainKeyword(@RequestParam String keyword,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			postService.deleteEventPostsByTitleContainKeyword(keyword).getMessage());
		return "redirect:/posts/event";
	}

	@AuthorizeRole("admin")
	@GetMapping("/sitemap")
	public String getSitemapPage() {
		return "user/admin/sitemap";
	}

	@PostMapping("/sitemap")
	public String createSitemap(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", postService.createSitemap().getMessage());
		return "redirect:/posts/sitemap";
	}
}