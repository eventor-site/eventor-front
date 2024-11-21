package com.eventorfront.attachment.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eventorfront.attachment.dto.request.CreateAttachmentRequest;

@FeignClient(name = "attachment-client", url = "http://localhost:8090/back/attachments")
public interface AttachmentClient {

	@PostMapping
	ResponseEntity<Void> createAttachment(@RequestBody CreateAttachmentRequest request);

	@DeleteMapping("/{attachmentId}")
	ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId);
}
