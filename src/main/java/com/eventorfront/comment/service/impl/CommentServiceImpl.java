package com.eventorfront.comment.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.comment.client.CommentClient;
import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.dto.response.GetCommentPageResponse;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.comment.service.CommentService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentClient commentClient;

	@Override
	public ApiResponse<Page<GetCommentResponse>> getCommentsByPostId(Pageable pageable, Long postId) {
		return commentClient.getCommentsByPostId(pageable, postId).getBody();
	}

	@Override
	public ApiResponse<Page<GetCommentByUserIdResponse>> getComments(Pageable pageable) {
		return commentClient.getComments(pageable).getBody();
	}

	@Override
	public ApiResponse<Page<GetCommentByUserIdResponse>> getCommentsByUserId(Pageable pageable) {
		return commentClient.getCommentsByUserId(pageable).getBody();
	}

	@Override
	public ApiResponse<GetCommentPageResponse> getComment(Long postId, Long commentId) {
		return commentClient.getComment(postId, commentId).getBody();
	}

	@Override
	public ApiResponse<Void> createComment(Long postId, CreateCommentRequest request) {
		return commentClient.createComment(postId, request).getBody();
	}

	@Override
	public ApiResponse<Void> updateComment(Long commentId, UpdateCommentRequest request) {
		return commentClient.updateComment(commentId, request).getBody();
	}

	@Override
	public ApiResponse<Void> recommendComment(Long commentId) {
		return commentClient.recommendComment(commentId).getBody();
	}

	@Override
	public ApiResponse<Void> disrecommendComment(Long commentId) {
		return commentClient.disrecommendComment(commentId).getBody();
	}

	@Override
	public ApiResponse<Void> deleteComment(Long commentId) {
		return commentClient.deleteComment(commentId).getBody();
	}
}
