package com.eventorfront.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TermController {

	@GetMapping("/terms/1")
	public String termsPage1() {
		return "term/policy1";
	}

	@GetMapping("/terms/2")
	public String termsPage2() {
		return "term/policy2";
	}

}
