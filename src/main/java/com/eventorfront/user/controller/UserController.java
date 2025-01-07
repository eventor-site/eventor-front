package com.eventorfront.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByAddShopResponse;
import com.eventorfront.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@GetMapping("/search")
	public ResponseEntity<List<GetUserByAddShopResponse>> searchUserById(@RequestParam String keyword) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.searchUserById(keyword));
	}

	@GetMapping("/admin")
	public String adminPage() {
		return "user/admin";
	}

	@GetMapping("/me")
	public String userPage(Model model) {
		model.addAttribute("user", userService.getUserInfo());
		return "user/me";
	}

	@GetMapping("/me/update")
	public String updateUserForm(Model model) {
		model.addAttribute("user", userService.getUserInfo());
		return "user/update";
	}

	@PutMapping("/me")
	public String updateUser(@ModelAttribute UpdateUserRequest request) {
		userService.updateUser(request);
		return "redirect:/users/me";
	}

}