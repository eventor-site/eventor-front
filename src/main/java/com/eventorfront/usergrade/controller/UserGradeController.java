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

import com.eventorfront.usergrade.dto.UserGradeDto;
import com.eventorfront.usergrade.service.UserGradeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user-grades")
public class UserGradeController {
	private final UserGradeService userGradeService;
	private static final String REDIRECT_URL = "redirect:/user-grades";

	@GetMapping("/create")
	public String createUserGradeForm() {
		return "user-grade/create";
	}

	@GetMapping("/update/{userGradeId}")
	public String updateUserGradeForm(@PathVariable Long userGradeId, Model model) {
		model.addAttribute("userGrade", userGradeService.getUserGrade(userGradeId));
		return "user-grade/update";
	}

	@GetMapping
	public String getUserGrades(Model model) {
		model.addAttribute("userGrades", userGradeService.getUserGrades());
		return "user-grade/list";
	}

	@PostMapping
	public String createUserGrade(@ModelAttribute UserGradeDto request) {
		userGradeService.createUserGrade(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{userGradeId}")
	public String updateUserGrade(@PathVariable Long userGradeId, @ModelAttribute UserGradeDto request) {
		userGradeService.updateUserGrade(userGradeId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{userGradeId}")
	public String deleteUserGrade(@PathVariable Long userGradeId) {
		userGradeService.deleteUserGrade(userGradeId);
		return REDIRECT_URL;
	}
}