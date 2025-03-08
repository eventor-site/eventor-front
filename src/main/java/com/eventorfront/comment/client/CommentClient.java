package com.eventorfront.comment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.dto.response.GetCommentPageResponse;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "comment-client", url = "${feignClient.url}")
public interface CommentClient {

	@GetMapping("/back/posts/{postId}/comments/paging")
	ApiResponse<Page<GetCommentResponse>> getCommentsByPostId(
		@PageableDefault(page = 1, size = 10) Pageable pageable, @PathVariable Long postId);

	@GetMapping("/back/users/admin/comments/paging")
	ApiResponse<Page<GetCommentByUserIdResponse>> getComments(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/users/me/comments/paging")
	ApiResponse<Page<GetCommentByUserIdResponse>> getCommentsByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/{postId}/comments/{commentId}")
	ApiResponse<GetCommentPageResponse> getComment(@PathVariable Long postId, @PathVariable Long commentId);

	@PostMapping("/back/posts/{postId}/comments")
	ApiResponse<Void> createComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request);

	@PutMapping("/back/posts/{postId}/comments/{commentId}")
	ApiResponse<Void> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request);

	@PutMapping("/back/posts/{postId}/comments/{commentId}/recommend")
	ApiResponse<Void> recommendComment(@PathVariable Long postId, @PathVariable Long commentId);

	@PutMapping("/back/posts/{postId}/comments/{commentId}/disrecommend")
	ApiResponse<Void> disrecommendComment(@PathVariable Long postId, @PathVariable Long commentId);

	@DeleteMapping("/back/posts/{postId}/comments/{commentId}")
	ApiResponse<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId);
}
