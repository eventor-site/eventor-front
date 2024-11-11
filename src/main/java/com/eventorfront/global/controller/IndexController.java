package com.eventorfront.global.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

	@GetMapping("/")
	public String indexPage() {
		return "index";
	}

	@GetMapping("/start")
	public String startPage() {
		return "start";
	}

	// @GetMapping("/")
	// public String indexPage() {
	// 	return "redirect:/main";
	// }

	@GetMapping("/api/users/admin")
	public String adminPage() {
		return "admin/admin-account";
	}

	@GetMapping("/api/users/me")
	public String userPage() {
		return "user/user-account";
	}

	@GetMapping("/template")
	public String showTemplate() {
		return "menu-template";
	}
}
