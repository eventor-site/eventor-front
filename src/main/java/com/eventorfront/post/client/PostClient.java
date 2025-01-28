package com.eventorfront.post.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
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

@FeignClient(name = "post-client", url = "http://localhost:8090/back/posts")
public interface PostClient {

	@GetMapping("/all")
	ResponseEntity<List<GetPostSimpleResponse>> getPosts();

	@GetMapping("/event/hot")
	ResponseEntity<List<GetMainPostResponse>> getHotEventPosts();

	@GetMapping("/event/latest")
	ResponseEntity<List<GetMainPostResponse>> getLatestEventPosts();

	@GetMapping("/event/recommendation")
	ResponseEntity<List<GetMainPostResponse>> getRecommendationEventPosts();

	@GetMapping("/hot")
	ResponseEntity<List<GetMainPostResponse>> getHotPostsByCategoryName(@RequestParam String categoryName);

	@GetMapping
	ApiResponse<List<GetPostsByCategoryNameResponse>> getPostsByCategoryName(@RequestParam String categoryName);

	@GetMapping("/me")
	ResponseEntity<List<GetPostSimpleResponse>> getPostsByUserId();

	@GetMapping("/{postId}")
	ResponseEntity<GetPostResponse> getPost(@PathVariable Long postId);

	@PostMapping
	ResponseEntity<CreatePostResponse> createPost(@RequestBody CreatePostRequest request);

	@PutMapping("/{postId}")
	ResponseEntity<Void> updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request);

	@PutMapping("/{postId}/recommend")
	ResponseEntity<String> recommendPost(@PathVariable Long postId);

	@PutMapping("/{postId}/disrecommend")
	ResponseEntity<String> disrecommendPost(@PathVariable Long postId);

	@DeleteMapping("/{postId}")
	ResponseEntity<Void> deletePost(@PathVariable Long postId);
}
