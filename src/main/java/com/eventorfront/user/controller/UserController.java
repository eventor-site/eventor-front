package com.eventorfront.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.auth.service.AuthService;
import com.eventorfront.global.util.CookieUtil;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByAddShopResponse;
import com.eventorfront.user.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final AuthService authService;

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

	@GetMapping("/me/modifyPasswordForm")
	public String modifyPasswordForm() {
		return "user/modifyPasswordForm";
	}

	@PostMapping("/signUp/checkIdentifier")
	public ResponseEntity<String> checkIdentifier(@ModelAttribute CheckIdentifierRequest request) {
		return userService.checkIdentifier(request);
	}

	@PutMapping("/me")
	public String updateUser(@ModelAttribute UpdateUserRequest request) {
		userService.updateUser(request);
		return "redirect:/users/me";
	}

	@PutMapping("/me/password")
	public String modifyPassword(@ModelAttribute ModifyPasswordRequest request) {
		userService.modifyPassword(request);
		return "redirect:/users/me";
	}

	@DeleteMapping("/me")
	public String withdrawUser(HttpServletResponse response) {
		userService.withdrawUser();
		authService.logout();
		CookieUtil.revokeToken(response, "Access-Token");
		CookieUtil.revokeToken(response, "Refresh-Token");
		return "redirect:/auth/login";
	}

}