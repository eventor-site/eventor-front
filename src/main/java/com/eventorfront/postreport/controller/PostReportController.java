package com.eventorfront.postreport.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.global.util.PagingModel;
import com.eventorfront.postreport.dto.response.GetPostReportResponse;
import com.eventorfront.postreport.service.PostReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostReportController {
	private final PostReportService postReportService;

	@GetMapping("/postReports")
	public String getPostReports(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPostReportResponse> postReports = postReportService.getPostReports(pageable);
		model.addAttribute("objects", postReports);
		PagingModel.pagingProcessing(pageable, model, postReports, "/postReports", 10);
		return "postReport/list";
	}

	@PostMapping("/posts/{postId}/postReports")
	public ResponseEntity<String> createPostReport(@PathVariable Long postId, @RequestParam String reportTypeName) {
		return postReportService.createPostReport(postId, reportTypeName);
	}

	@GetMapping("/posts/{postId}/postReports/{postReportId}/confirm")
	public String confirmPostReport(@PathVariable Long postId, @PathVariable Long postReportId) {
		postReportService.confirmPostReport(postId, postReportId);
		return "redirect:/posts/" + postId;
	}

	@DeleteMapping("/postReports/{postReportId}")
	public String deletePostReport(@PathVariable Long postReportId) {
		postReportService.deletePostReport(postReportId);
		return "redirect:/postReports";
	}
}
