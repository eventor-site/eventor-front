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

import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.auth.service.AuthService;
import com.eventorfront.global.exception.AccessDeniedException;
import com.eventorfront.global.util.CookieUtil;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.SendCodeRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserByUserId;
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
	public ResponseEntity<List<GetUserByIdentifier>> searchUserByIdentifier(@RequestParam String keyword) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.searchUserByIdentifier(keyword));
	}

	@GetMapping("/search/userId")
	public ResponseEntity<List<GetUserByUserId>> searchUserByUserId(@RequestParam Long userId) {
		return ResponseEntity.status(HttpStatus.OK).body(userService.searchUserByUserId(userId));
	}

	@GetMapping("/recover/identifier")
	public String recoverIdentifierPage() {
		return "user/recoverIdentifierForm";
	}

	@GetMapping("/recover/password")
	public String recoverPasswordPage() {
		return "user/recoverPasswordForm";
	}

	@GetMapping("/admin")
	public String adminPage() {
		if (userService.meCheckRoles("admin")) {
			return "user/admin";
		} else {
			throw new AccessDeniedException();
		}
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

	@PutMapping("/me")
	public String updateUser(@ModelAttribute UpdateUserRequest request) {
		userService.updateUser(request);
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

	@PostMapping("/me/checkNickname")
	ResponseEntity<String> meCheckNickname(@ModelAttribute CheckNicknameRequest request) {
		return userService.meCheckNickname(request);
	}

	@PutMapping("/me/password")
	public String modifyPassword(@ModelAttribute ModifyPasswordRequest request) {
		userService.modifyPassword(request);
		return "redirect:/users/me";
	}

	@GetMapping("/me/Roles")
	public ResponseEntity<List<String>> getRoles() {
		return ResponseEntity.ok(userService.meRoles());
	}

	@GetMapping("/signup")
	public String signup() {
		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute SignUpRequest request) {
		userService.signup(request);
		return "redirect:/auth/login";
	}

	@PostMapping("/signup/checkIdentifier")
	public ResponseEntity<String> checkIdentifier(@ModelAttribute CheckIdentifierRequest request) {
		return userService.checkIdentifier(request);
	}

	@PostMapping("/signup/checkNickname")
	public ResponseEntity<String> checkNickname(@ModelAttribute CheckNicknameRequest request) {
		return userService.checkNickname(request);
	}

	@PostMapping("/signup/sendEmail")
	ResponseEntity<String> sendEmail(@ModelAttribute SendCodeRequest request) {
		return userService.sendEmail(request);
	}

	@GetMapping("/signup/checkEmail")
	ResponseEntity<String> checkEmail(@RequestParam("email") String email,
		@RequestParam("certifyCode") String certifyCode) {
		return userService.checkEmail(email, certifyCode);
	}

	@PostMapping("/recover/identifier")
	ResponseEntity<String> recoverIdentifier(@RequestParam String email) {
		return userService.recoverIdentifier(email);
	}

	@PostMapping("/recover/password")
	ResponseEntity<String> recoverPassword(@RequestParam String identifier) {
		return userService.recoverPassword(identifier);
	}

}