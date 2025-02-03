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
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.postreport.dto.response.GetPostReportResponse;

@FeignClient(name = "postReport-client", url = "http://localhost:8090/back")
public interface PostReportClient {

	@GetMapping("/postReports/paging")
	ResponseEntity<Page<GetPostReportResponse>> getPostReports(@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/posts/{postId}/postReports")
	ResponseEntity<String> createPostReport(@PathVariable Long postId, @RequestParam String reportTypeName);

	@GetMapping("/posts/{postId}/postReports/{postReportId}/confirm")
	ResponseEntity<Void> confirmPostReport(@PathVariable Long postId, @PathVariable Long postReportId);

	@DeleteMapping("/postReports/{postReportId}")
	ResponseEntity<String> deletePostReport(@PathVariable Long postReportId);
}
