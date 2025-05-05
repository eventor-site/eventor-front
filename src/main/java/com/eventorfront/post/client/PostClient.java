package com.eventorfront.post.client;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetEventPostCountByAdminResponse;
import com.eventorfront.post.dto.response.GetMainHotPostResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.dto.response.GetPostsByCategoryNameResponse;
import com.eventorfront.post.dto.response.GetRecommendPostResponse;
import com.eventorfront.post.dto.response.GetTempPostResponse;
import com.eventorfront.search.dto.response.SearchPostsResponse;

@FeignClient(name = "post-client", url = "${feignClient.url}")
public interface PostClient {

	@GetMapping("/back/posts/search")
	ResponseEntity<ApiResponse<Page<SearchPostsResponse>>> searchPosts(
		@PageableDefault(page = 1, size = 10, sort = "createdAt,desc") Pageable pageable,
		@RequestParam(defaultValue = "") String keyword,
		@RequestParam(defaultValue = "") String categoryName,
		@RequestParam(defaultValue = "") String eventStatusName,
		@RequestParam(defaultValue = "") String endType);

	@GetMapping("/back/posts/all/paging")
	ResponseEntity<ApiResponse<Page<GetPostSimpleResponse>>> getPosts(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/event/special/notice/paging")
	ResponseEntity<ApiResponse<Page<GetPostSimpleResponse>>> getSpecialNoticeEventPosts(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/event/hot")
	ResponseEntity<ApiResponse<List<GetMainHotPostResponse>>> getHotEventPosts();

	@GetMapping("/back/posts/event/latest")
	ResponseEntity<ApiResponse<List<GetMainPostResponse>>> getLatestEventPosts();

	@GetMapping("/back/posts/event/deadline")
	ResponseEntity<ApiResponse<List<GetMainPostResponse>>> getDeadlineEventPosts();

	@GetMapping("/back/posts/event/recommendation")
	ResponseEntity<ApiResponse<List<GetRecommendPostResponse>>> getRecommendationEventPosts();

	@GetMapping("/back/posts/event/trending")
	ResponseEntity<ApiResponse<List<GetRecommendPostResponse>>> getTrendingEventPosts();

	@GetMapping("/back/posts/community")
	public ResponseEntity<ApiResponse<List<GetMainPostResponse>>> getCommunityPosts();

	@GetMapping("/back/posts/hot")
	ResponseEntity<ApiResponse<List<GetMainPostResponse>>> getHotPostsByCategoryName(@RequestParam String categoryName);

	@GetMapping("/back/posts")
	ResponseEntity<ApiResponse<Page<GetPostsByCategoryNameResponse>>> getPostsByCategoryName(
		@PageableDefault(page = 1, size = 10, sort = "createdAt,desc") Pageable pageable,
		@RequestParam(defaultValue = "") String categoryName,
		@RequestParam(defaultValue = "") String eventStatusName,
		@RequestParam(defaultValue = "") String endType);

	@GetMapping("/back/posts/me/paging")
	ResponseEntity<ApiResponse<Page<GetPostSimpleResponse>>> getPostsByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/{postId}")
	ResponseEntity<ApiResponse<GetPostResponse>> getPost(@RequestParam(required = false) String uuid,
		@PathVariable Long postId);

	@GetMapping("/back/posts/temp")
	ResponseEntity<ApiResponse<GetTempPostResponse>> getTempPost();

	@PostMapping("/back/posts")
	ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@RequestBody CreatePostRequest request,
		@RequestParam boolean isTemp);

	@PutMapping("/back/posts/{postId}")
	ResponseEntity<ApiResponse<Void>> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request,
		@RequestParam boolean isTemp);

	@PutMapping("/back/posts/{postId}/finish")
	ResponseEntity<ApiResponse<Void>> finishEventPost(@PathVariable Long postId);

	@PutMapping("/back/posts/{postId}/recommend")
	ResponseEntity<ApiResponse<Void>> recommendPost(@PathVariable Long postId);

	@PutMapping("/back/posts/{postId}/disrecommend")
	ResponseEntity<ApiResponse<Void>> disrecommendPost(@PathVariable Long postId);

	@DeleteMapping("/back/posts/{postId}")
	ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long postId);

	@GetMapping("/back/posts/{postId}/isAuthorized")
	ResponseEntity<ApiResponse<Boolean>> isAuthorizedToEdit(@PathVariable Long postId);

	@DeleteMapping("/back/posts/temp")
	ResponseEntity<ApiResponse<Void>> deleteTempPost();

	@GetMapping("/back/posts/statistic/users/admin")
	ResponseEntity<ApiResponse<List<GetEventPostCountByAdminResponse>>> getEventPostCountByAdmin(
		@RequestParam(required = false) LocalDateTime startTime,
		@RequestParam(required = false) LocalDateTime endTime);

	@DeleteMapping("/back/posts/event")
	ResponseEntity<ApiResponse<Void>> deleteEventPostsByTitleContainKeyword(
		@RequestParam String keyword);
}
