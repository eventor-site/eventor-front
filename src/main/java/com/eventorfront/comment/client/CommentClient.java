package com.eventorfront.comment.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentResponse;

@FeignClient(name = "comment-client", url = "http://localhost:8090/back/posts")
public interface CommentClient {

	@GetMapping("/{postId}/comments")
	ResponseEntity<List<GetCommentResponse>> getCommentsByPostId(@PathVariable Long postId);

	@PostMapping("/{postId}/comments")
	ResponseEntity<Void> createComment(@PathVariable Long postId, @RequestBody CreateCommentRequest request);

	@PutMapping("/{postId}/comments/{commentId}")
	ResponseEntity<Void> updateComment(@PathVariable Long postId, @PathVariable Long commentId,
		@RequestBody UpdateCommentRequest request);

	@PutMapping("/{postId}/comments/{commentId}/recommend")
	ResponseEntity<String> recommendComment(@PathVariable Long postId, @PathVariable Long commentId);

	@PutMapping("/{postId}/comments/{commentId}/disrecommend")
	ResponseEntity<String> disrecommendComment(@PathVariable Long postId, @PathVariable Long commentId);

	@DeleteMapping("/{postId}/comments/{commentId}")
	ResponseEntity<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId);
}
