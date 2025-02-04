package com.eventorfront.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.dto.response.GetCommentPageResponse;
import com.eventorfront.comment.dto.response.GetCommentResponse;

public interface CommentService {

	Page<GetCommentResponse> getCommentsByPostId(Pageable pageable, Long postId);

	Page<GetCommentByUserIdResponse> getComments(Pageable pageable);

	Page<GetCommentByUserIdResponse> getCommentsByUserId(Pageable pageable);

	GetCommentPageResponse getComment(Long postId, Long commentId);

	void createComment(Long postId, CreateCommentRequest request);

	void updateComment(Long postId, Long commentId, UpdateCommentRequest request);

	ResponseEntity<String> recommendComment(Long postId, Long commentId);

	ResponseEntity<String> disrecommendComment(Long postId, Long commentId);

	void deleteComment(Long postId, Long commentId);
}
