package com.eventorfront.usergrade.controller;

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
import com.eventorfront.usergrade.dto.GradeDto;
import com.eventorfront.usergrade.service.GradeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/grades")
public class GradeController {
	private final GradeService gradeService;
	private static final String REDIRECT_URL = "redirect:/grades";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createGradeForm() {
		return "grade/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/update/{gradeId}")
	public String updateGradeForm(@PathVariable Long gradeId, Model model) {
		model.addAttribute("grade", gradeService.getGrade(gradeId));
		return "grade/update";
	}

	@GetMapping
	public String getGrades(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GradeDto> grades = gradeService.getGrades(pageable);
		model.addAttribute("objects", grades);
		PagingModel.pagingProcessing(pageable, model, grades, "/grades", 10);
		return "grade/list";
	}

	@PostMapping
	public String createGrade(@ModelAttribute GradeDto request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", gradeService.createGrade(request));
		return REDIRECT_URL;
	}

	@PutMapping("/{gradeId}")
	public String updateGrade(@PathVariable Long gradeId, @ModelAttribute GradeDto request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", gradeService.updateGrade(gradeId, request));
		return REDIRECT_URL;
	}

	@DeleteMapping("/{gradeId}")
	public String deleteGrade(@PathVariable Long gradeId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", gradeService.deleteGrade(gradeId));
		return REDIRECT_URL;
	}
}