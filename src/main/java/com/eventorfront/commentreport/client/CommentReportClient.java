package com.eventorfront.commentreport.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;

@FeignClient(name = "commentReport-client", url = "http://localhost:8090/back")
public interface CommentReportClient {

	@GetMapping("/commentReports")
	ResponseEntity<List<GetCommentReportResponse>> getCommentReports();

	@PostMapping("/comments/{commentId}/commentReports")
	ResponseEntity<String> createCommentReport(@PathVariable Long commentId, @RequestParam String reportTypeName);

	@DeleteMapping("/commentReports/{commentReportId}")
	ResponseEntity<String> deleteCommentReport(@PathVariable Long commentReportId);
}
