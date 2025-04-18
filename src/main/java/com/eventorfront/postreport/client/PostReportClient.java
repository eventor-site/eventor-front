package com.eventorfront.postreport.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.postreport.dto.response.GetPostReportResponse;

@FeignClient(name = "postReport-client", url = "${feignClient.url}")
public interface PostReportClient {

	@GetMapping("/back/postReports/paging")
	ResponseEntity<ApiResponse<Page<GetPostReportResponse>>> getPostReports(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/back/posts/{postId}/postReports")
	ResponseEntity<ApiResponse<Void>> createPostReport(@PathVariable Long postId, @RequestParam String reportTypeName);

	@PutMapping("/back/postReports/{postReportId}/confirm")
	ResponseEntity<ApiResponse<Void>> confirmPostReport(@PathVariable Long postReportId);

	@DeleteMapping("/back/postReports/{postReportId}")
	ResponseEntity<ApiResponse<Void>> deletePostReport(@PathVariable Long postReportId);
}
