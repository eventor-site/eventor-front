package com.eventorfront.commentreport.controller;

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

import com.eventorfront.commentreport.dto.response.GetCommentReportResponse;
import com.eventorfront.commentreport.service.CommentReportService;
import com.eventorfront.global.util.PagingModel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class CommentReportController {
	private final CommentReportService commentReportService;

	@GetMapping("/commentReports")
	public String getCommentReports(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetCommentReportResponse> commentReports = commentReportService.getCommentReports(pageable);
		model.addAttribute("objects", commentReports);
		PagingModel.pagingProcessing(pageable, model, commentReports, "/commentReports", 10);
		return "commentReport/list";
	}

	@PostMapping("/comments/{commentId}/commentReports")
	public ResponseEntity<String> createCommentReport(@PathVariable Long commentId,
		@RequestParam String reportTypeName) {
		return commentReportService.createCommentReport(commentId, reportTypeName);
	}

	@GetMapping("/posts/{postId}/comments/{commentId}/commentReports/{commentReportId}/confirm")
	public String confirmCommentReport(@PathVariable Long postId, @PathVariable Long commentId,
		@PathVariable Long commentReportId) {
		commentReportService.confirmCommentReport(postId, commentReportId, commentReportId);
		return "redirect:/posts/" + postId + "#" + commentId;
	}

	@DeleteMapping("/commentReports/{commentReportId}")
	public String deleteCommentReport(@PathVariable Long commentReportId) {
		commentReportService.deleteCommentReport(commentReportId);
		return "redirect:/commentReports";
	}
}
