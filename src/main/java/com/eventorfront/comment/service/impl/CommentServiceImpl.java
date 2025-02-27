package com.eventorfront.comment.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.eventorfront.comment.client.CommentClient;
import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.dto.response.GetCommentPageResponse;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentClient commentClient;

	@Override
	public Page<GetCommentResponse> getCommentsByPostId(Pageable pageable, Long postId) {
		return commentClient.getCommentsByPostId(pageable, postId).getBody();
	}

	@Override
	public Page<GetCommentByUserIdResponse> getComments(Pageable pageable) {
		return commentClient.getComments(pageable).getBody();
	}

	@Override
	public Page<GetCommentByUserIdResponse> getCommentsByUserId(Pageable pageable) {
		return commentClient.getCommentsByUserId(pageable).getBody();
	}

	@Override
	public GetCommentPageResponse getComment(Long postId, Long commentId) {
		return commentClient.getComment(postId, commentId).getBody();
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
