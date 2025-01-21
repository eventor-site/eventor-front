package com.eventorfront.postreport.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.postreport.dto.response.GetPostReportResponse;

@FeignClient(name = "postReport-client", url = "http://localhost:8090/back")
public interface PostReportClient {

	@GetMapping("/postReports")
	ResponseEntity<List<GetPostReportResponse>> getPostReports();

	@PostMapping("/posts/{postId}/postReports")
	ResponseEntity<String> createPostReport(@PathVariable Long postId, @RequestParam String reportTypeName);

	@GetMapping("/posts/{postId}/postReports/{postReportId}/confirm")
	ResponseEntity<Void> confirmPostReport(@PathVariable Long postId, @PathVariable Long postReportId);

	@DeleteMapping("/postReports/{postReportId}")
	ResponseEntity<String> deletePostReport(@PathVariable Long postReportId);
}
