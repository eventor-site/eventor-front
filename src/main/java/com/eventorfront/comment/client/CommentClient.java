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

@FeignClient(name = "comment-client", url = "http://localhost:8090/back")
public interface CommentClient {

	@GetMapping("/posts/{postId}/comments/paging")
	ResponseEntity<Page<GetCommentResponse>> getCommentsByPostId(
		@PageableDefault(page = 1, size = 10) Pageable pageable, @PathVariable Long postId);

	@GetMapping("/users/admin/comments/paging")
	ResponseEntity<Page<GetCommentByUserIdResponse>> getComments(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/users/me/comments/paging")
	ResponseEntity<Page<GetCommentByUserIdResponse>> getCommentsByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/posts/{postId}/comments/{commentId}")
	ResponseEntity<GetCommentPageResponse> getComment(@PathVariable Long postId, @PathVariable Long commentId);

	@PostMapping("/posts/{postId}/comments")
	ResponseEntity<Void> createComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request);

	@PutMapping("/posts/{postId}/comments/{commentId}")
	ResponseEntity<Void> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request);

	@PutMapping("/posts/{postId}/comments/{commentId}/recommend")
	ResponseEntity<String> recommendComment(@PathVariable Long postId, @PathVariable Long commentId);

	@PutMapping("/posts/{postId}/comments/{commentId}/disrecommend")
	ResponseEntity<String> disrecommendComment(@PathVariable Long postId, @PathVariable Long commentId);

	@DeleteMapping("/posts/{postId}/comments/{commentId}")
	ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId);
}
