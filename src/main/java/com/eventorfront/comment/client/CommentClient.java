package com.eventorfront.comment.client;

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

import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.dto.response.GetCommentPageResponse;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "comment-client", url = "${feignClient.url}")
public interface CommentClient {

	@GetMapping("/back/posts/{postId}/comments/paging")
	ResponseEntity<ApiResponse<Page<GetCommentResponse>>> getCommentsByPostId(
		@PageableDefault(page = 1, size = 10) Pageable pageable, @PathVariable Long postId);

	@GetMapping("/back/users/admin/comments/paging")
	ResponseEntity<ApiResponse<Page<GetCommentByUserIdResponse>>> getComments(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/users/me/comments/paging")
	ResponseEntity<ApiResponse<Page<GetCommentByUserIdResponse>>> getCommentsByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/posts/{postId}/comments/{commentId}")
	ResponseEntity<ApiResponse<GetCommentPageResponse>> getComment(@PathVariable Long postId,
		@PathVariable Long commentId);

	@PostMapping("/back/posts/{postId}/comments")
	ResponseEntity<ApiResponse<Void>> createComment(@PathVariable Long postId,
		@RequestBody CreateCommentRequest request);

	@PutMapping("/back/comments/{commentId}")
	ResponseEntity<ApiResponse<Void>> updateComment(@PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request);

	@PutMapping("/back/comments/{commentId}/recommend")
	ResponseEntity<ApiResponse<Void>> recommendComment(@PathVariable Long commentId);

	@PutMapping("/back/comments/{commentId}/disrecommend")
	ResponseEntity<ApiResponse<Void>> disrecommendComment(@PathVariable Long commentId);

	@DeleteMapping("/back/comments/{commentId}")
	ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long commentId);
}
