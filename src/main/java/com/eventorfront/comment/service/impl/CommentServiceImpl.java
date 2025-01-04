package com.eventorfront.comment.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.comment.client.CommentClient;
import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentClient commentClient;

	@Override
	public List<GetCommentResponse> getCommentsByPostId(Long postId) {
		return commentClient.getCommentsByPostId(postId).getBody();
	}

	@Override
	public void createComment(Long postId, CreateCommentRequest request) {
		commentClient.createComment(postId, request);
	}

	@Override
	public void updateComment(Long postId, Long commentId, UpdateCommentRequest request) {
		commentClient.updateComment(postId, commentId, request);
	}

	@Override
	public ResponseEntity<String> recommendComment(Long postId, Long commentId) {
		return commentClient.recommendComment(postId, commentId);
	}

	@Override
	public ResponseEntity<String> disrecommendComment(Long postId, Long commentId) {
		return commentClient.disrecommendComment(postId, commentId);
	}

	@Override
	public void deleteComment(Long postId, Long commentId) {
		commentClient.deleteComment(postId, commentId);
	}
}
