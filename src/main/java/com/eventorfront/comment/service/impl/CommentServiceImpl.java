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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentClient commentClient;

	@Override
	public Page<GetCommentResponse> getCommentsByPostId(Pageable pageable, Long postId) {
		return commentClient.getCommentsByPostId(pageable, postId).getData();
	}

	@Override
	public Page<GetCommentByUserIdResponse> getComments(Pageable pageable) {
		return commentClient.getComments(pageable).getData();
	}

	@Override
	public Page<GetCommentByUserIdResponse> getCommentsByUserId(Pageable pageable) {
		return commentClient.getCommentsByUserId(pageable).getData();
	}

	@Override
	public GetCommentPageResponse getComment(Long postId, Long commentId) {
		return commentClient.getComment(postId, commentId).getData();
	}

	@Override
	public String createComment(Long postId, CreateCommentRequest request) {
		return commentClient.createComment(postId, request).getMessage();
	}

	@Override
	public String updateComment(Long postId, Long commentId, UpdateCommentRequest request) {
		return commentClient.updateComment(postId, commentId, request).getMessage();
	}

	@Override
	public String recommendComment(Long postId, Long commentId) {
		return commentClient.recommendComment(postId, commentId).getMessage();
	}

	@Override
	public String disrecommendComment(Long postId, Long commentId) {
		return commentClient.disrecommendComment(postId, commentId).getMessage();
	}

	@Override
	public String deleteComment(Long postId, Long commentId) {
		return commentClient.deleteComment(postId, commentId).getMessage();
	}
}
