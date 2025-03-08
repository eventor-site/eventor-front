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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
		Page<GetPostReportResponse> postReports = postReportService.getPostReports(pageable).getData();
		model.addAttribute("objects", postReports);
		PagingModel.pagingProcessing(pageable, model, postReports, "/postReports", 10);
		return "postReport/list";
	}

	@PostMapping("/posts/{postId}/postReports")
	public ResponseEntity<String> createPostReport(@PathVariable Long postId, @RequestParam String reportTypeName) {
		return ResponseEntity.ok(postReportService.createPostReport(postId, reportTypeName).getMessage());
	}

	@GetMapping("/posts/{postId}/postReports/{postReportId}/confirm")
	public String confirmPostReport(@PathVariable Long postId, @PathVariable Long postReportId,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			postReportService.confirmPostReport(postId, postReportId).getMessage());
		return "redirect:/posts/" + postId;
	}

	@DeleteMapping("/postReports/{postReportId}")
	public String deletePostReport(@PathVariable Long postReportId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", postReportService.deletePostReport(postReportId).getMessage());
		return "redirect:/postReports";
	}
}
