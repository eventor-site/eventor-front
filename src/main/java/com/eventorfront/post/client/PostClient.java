package com.eventorfront.post.client;

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
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.dto.response.GetPostsByCategoryNameResponse;
import com.eventorfront.post.dto.response.GetRecommendPostResponse;
import com.eventorfront.search.dto.response.SearchPostsResponse;

@FeignClient(name = "post-client", url = "${feignClient.url}")
public interface PostClient {

	@GetMapping("/back/posts/search")
	ResponseEntity<Page<SearchPostsResponse>> searchPosts(@PageableDefault(page = 1, size = 10) Pageable pageable,
		@RequestParam String keyword);

	@GetMapping("/back/posts/all/paging")
	ResponseEntity<Page<GetPostSimpleResponse>> getPosts(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/event/hot")
	ResponseEntity<List<GetMainPostResponse>> getHotEventPosts();

	@GetMapping("/back/posts/event/latest")
	ResponseEntity<List<GetMainPostResponse>> getLatestEventPosts();

	@GetMapping("/back/posts/event/recommendation")
	ResponseEntity<List<GetRecommendPostResponse>> getRecommendationEventPosts();

	@GetMapping("/back/posts/event/trending")
	ResponseEntity<List<GetRecommendPostResponse>> getTrendingEventPosts();

	@GetMapping("/back/posts/hot")
	ResponseEntity<List<GetMainPostResponse>> getHotPostsByCategoryName(@RequestParam String categoryName);

	@GetMapping("/back/posts")
	ApiResponse<Page<GetPostsByCategoryNameResponse>> getPostsByCategoryName(
		@PageableDefault(page = 1, size = 10) Pageable pageable, @RequestParam String categoryName);

	@GetMapping("/back/posts/me/paging")
	ResponseEntity<Page<GetPostSimpleResponse>> getPostsByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/{postId}")
	ResponseEntity<GetPostResponse> getPost(@PathVariable Long postId);

	@PostMapping("/back/posts")
	ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request);

	@PutMapping("/back/posts/{postId}")
	ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request);

	@PutMapping("/back/posts/{postId}/recommend")
	ResponseEntity<String> recommendPost(@PathVariable Long postId);

	@PutMapping("/{postId}/disrecommend")
	ResponseEntity<String> disrecommendPost(@PathVariable Long postId);

	@DeleteMapping("/{postId}")
	ResponseEntity<Void> deletePost(@PathVariable Long postId);
}
