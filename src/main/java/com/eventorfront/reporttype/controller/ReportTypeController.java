package com.eventorfront.reporttype.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.reporttype.dto.ReportTypeDto;
import com.eventorfront.reporttype.service.ReportTypeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reportTypes")
public class ReportTypeController {
	private final ReportTypeService reportTypeService;
	private static final String REDIRECT_URL = "redirect:/reportTypes";

	@GetMapping("/create")
	public String createReportTypeForm() {
		return "reportType/create";
	}

	@GetMapping("/{reportTypeId}/update")
	public String updateReportTypeForm(@PathVariable Long reportTypeId, Model model) {
		model.addAttribute("reportType", reportTypeService.getReportType(reportTypeId));
		return "reportType/update";
	}

	@GetMapping
	public String getReportTypes(Model model) {
		model.addAttribute("reportTypes", reportTypeService.getReportTypes());
		return "reportType/list";
	}

	@PostMapping
	public String createReportType(@ModelAttribute ReportTypeDto request) {
		reportTypeService.createReportType(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{reportTypeId}")
	public String updateReportType(@PathVariable Long reportTypeId, @ModelAttribute ReportTypeDto request) {
		reportTypeService.updateReportType(reportTypeId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{reportTypeId}")
	public String deleteReportType(@PathVariable Long reportTypeId) {
		reportTypeService.deleteReportType(reportTypeId);
		return REDIRECT_URL;
	}
}