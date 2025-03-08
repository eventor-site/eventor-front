package com.eventorfront.commentreport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;
import com.eventorfront.global.dto.ApiResponse;

@FeignClient(name = "commentReport-client", url = "${feignClient.url}")
public interface CommentReportClient {

	@GetMapping("/back/commentReports/paging")
	ResponseEntity<ApiResponse<Page<GetCommentReportResponse>>> getCommentReports(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/back/comments/{commentId}/commentReports")
	ResponseEntity<ApiResponse<Void>> createCommentReport(@PathVariable Long commentId,
		@RequestParam String reportTypeName);

	@GetMapping("/back/posts/{postId}/comments/{commentId}/commentReports/{commentReportId}/confirm")
	ResponseEntity<ApiResponse<Void>> confirmCommentReport(@PathVariable Long postId, @PathVariable Long commentId,
		@PathVariable Long commentReportId);

	@DeleteMapping("/back/commentReports/{commentReportId}")
	ResponseEntity<ApiResponse<Void>> deleteCommentReport(@PathVariable Long commentReportId);
}
