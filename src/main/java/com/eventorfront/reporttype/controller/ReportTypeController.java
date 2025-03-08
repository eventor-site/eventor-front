package com.eventorfront.reporttype.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.reporttype.dto.ReportTypeDto;
import com.eventorfront.reporttype.service.ReportTypeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reportTypes")
public class ReportTypeController {
	private final ReportTypeService reportTypeService;
	private static final String REDIRECT_URL = "redirect:/reportTypes";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createReportTypeForm() {
		return "reportType/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/{reportTypeId}/update")
	public String updateReportTypeForm(@PathVariable Long reportTypeId, Model model) {
		model.addAttribute("reportType", reportTypeService.getReportType(reportTypeId));
		return "reportType/update";
	}

	@GetMapping
	public String getReportTypes(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<ReportTypeDto> reportTypes = reportTypeService.getReportTypes(pageable);
		model.addAttribute("objects", reportTypes);
		PagingModel.pagingProcessing(pageable, model, reportTypes, "/reportTypes", 10);
		return "reportType/list";
	}

	@PostMapping
	public String createReportType(@ModelAttribute ReportTypeDto request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", reportTypeService.createReportType(request));
		return REDIRECT_URL;
	}

	@PutMapping("/{reportTypeId}")
	public String updateReportType(@PathVariable Long reportTypeId, @ModelAttribute ReportTypeDto request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", reportTypeService.updateReportType(reportTypeId, request));
		return REDIRECT_URL;
	}

	@DeleteMapping("/{reportTypeId}")
	public String deleteReportType(@PathVariable Long reportTypeId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", reportTypeService.deleteReportType(reportTypeId));
		return REDIRECT_URL;
	}
}