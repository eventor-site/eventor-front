package com.eventorfront.commentreport.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.commentreport.service.CommentReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentReportController {
	private final CommentReportService commentReportService;

	@GetMapping("/commentReports")
	public String getCommentReports(Model model) {
		model.addAttribute("commentReports", commentReportService.getCommentReports());
		return "commentReport/list";
	}

	@PostMapping("/comments/{commentId}/commentReports")
	public ResponseEntity<String> createCommentReport(@PathVariable Long commentId,
		@RequestParam String reportTypeName) {
		return commentReportService.createCommentReport(commentId, reportTypeName);
	}

	@DeleteMapping("/commentReports/{commentReportId}")
	public String deleteCommentReport(@PathVariable Long commentReportId) {
		commentReportService.deleteCommentReport(commentReportId);
		return "redirect:/commentReports";
	}
}
