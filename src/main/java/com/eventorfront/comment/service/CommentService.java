package com.eventorfront.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentByUserIdResponse;
import com.eventorfront.comment.dto.response.GetCommentPageResponse;
import com.eventorfront.comment.dto.response.GetCommentResponse;
import com.eventorfront.global.dto.ApiResponse;

public interface CommentService {

	ApiResponse<Page<GetCommentResponse>> getCommentsByPostId(Pageable pageable, Long postId);

	ApiResponse<Page<GetCommentByUserIdResponse>> getComments(Pageable pageable);

	ApiResponse<Page<GetCommentByUserIdResponse>> getCommentsByUserId(Pageable pageable);

	ApiResponse<GetCommentPageResponse> getComment(Long postId, Long commentId);

	ApiResponse<Void> createComment(Long postId, CreateCommentRequest request);

	ApiResponse<Void> updateComment(Long commentId, UpdateCommentRequest request);

	ApiResponse<Void> recommendComment(Long commentId);

	ApiResponse<Void> disrecommendComment(Long commentId);

	ApiResponse<Void> deleteComment(Long commentId);
}
