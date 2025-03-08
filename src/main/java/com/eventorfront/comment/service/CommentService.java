package com.eventorfront.comment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

	String createComment(Long postId, CreateCommentRequest request);

	String updateComment(Long postId, Long commentId, UpdateCommentRequest request);

	String recommendComment(Long postId, Long commentId);

	String disrecommendComment(Long postId, Long commentId);

	String deleteComment(Long postId, Long commentId);
}
