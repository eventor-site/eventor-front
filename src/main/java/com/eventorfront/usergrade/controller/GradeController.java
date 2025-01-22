package com.eventorfront.usergrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.usergrade.dto.GradeDto;
import com.eventorfront.usergrade.service.GradeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/grades")
public class GradeController {
	private final GradeService gradeService;
	private static final String REDIRECT_URL = "redirect:/grades";

	@GetMapping("/create")
	public String createGradeForm() {
		return "grade/create";
	}

	@GetMapping("/update/{gradeId}")
	public String updateGradeForm(@PathVariable Long gradeId, Model model) {
		model.addAttribute("grade", gradeService.getGrade(gradeId));
		return "grade/update";
	}

	@GetMapping
	public String getGrades(Model model) {
		model.addAttribute("grades", gradeService.getGrades());
		return "grade/list";
	}

	@PostMapping
	public String createGrade(@ModelAttribute GradeDto request) {
		gradeService.createGrade(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{gradeId}")
	public String updateGrade(@PathVariable Long gradeId, @ModelAttribute GradeDto request) {
		gradeService.updateGrade(gradeId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{gradeId}")
	public String deleteGrade(@PathVariable Long gradeId) {
		gradeService.deleteGrade(gradeId);
		return REDIRECT_URL;
	}
}