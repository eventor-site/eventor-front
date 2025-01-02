package com.eventorfront.comment.dto.request;

public record CreateCommentRequest(
	Long parentCommentId,
	String content) {
}
