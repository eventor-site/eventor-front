package com.eventorfront.comment.service;

import java.util.List;

import com.eventorfront.comment.dto.request.CreateCommentRequest;
import com.eventorfront.comment.dto.request.UpdateCommentRequest;
import com.eventorfront.comment.dto.response.GetCommentResponse;

public interface CommentService {

	List<GetCommentResponse> getCommentsByPostId(Long postId);

	void createComment(Long postId, CreateCommentRequest request);

	void updateComment(Long postId, Long commentId, UpdateCommentRequest request);

	void recommendComment(Long postId, Long commentId);

	void disrecommendComment(Long postId, Long commentId);

	void deleteComment(Long postId, Long commentId);
}
