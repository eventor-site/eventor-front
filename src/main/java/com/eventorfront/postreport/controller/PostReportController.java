package com.eventorfront.postreport.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.postreport.service.PostReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class PostReportController {
	private final PostReportService postReportService;

	@GetMapping("/postReports")
	public String getPostReports(Model model) {
		model.addAttribute("postReports", postReportService.getPostReports());
		return "postReport/list";
	}

	@PostMapping("/posts/{postId}/postReports")
	public ResponseEntity<String> createPostReport(@PathVariable Long postId, @RequestParam String reportTypeName) {
		return postReportService.createPostReport(postId, reportTypeName);
	}

	@DeleteMapping("/postReports/{postReportId}")
	public String deletePostReport(@PathVariable Long postReportId) {
		postReportService.deletePostReport(postReportId);
		return "redirect:/postReports";
	}
}
