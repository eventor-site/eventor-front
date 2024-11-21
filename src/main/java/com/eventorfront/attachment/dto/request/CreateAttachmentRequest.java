package com.eventorfront.attachment.dto.request;

import lombok.Builder;

@Builder
public record CreateAttachmentRequest(
	Long imageId,
	Long postId) {
}
