package com.eventorfront.user.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.auth.dto.request.SignUpRequest;
import com.eventorfront.auth.service.AuthService;
import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.global.util.CookieUtil;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.status.service.StatusService;
import com.eventorfront.user.dto.request.CertifyEmailRequest;
import com.eventorfront.user.dto.request.CheckIdentifierRequest;
import com.eventorfront.user.dto.request.CheckNicknameRequest;
import com.eventorfront.user.dto.request.ModifyPasswordRequest;
import com.eventorfront.user.dto.request.SendCodeRequest;
import com.eventorfront.user.dto.request.UpdateUserAttributeRequest;
import com.eventorfront.user.dto.request.UpdateUserRequest;
import com.eventorfront.user.dto.response.GetUserByIdentifier;
import com.eventorfront.user.dto.response.GetUserByUserId;
import com.eventorfront.user.dto.response.GetUserListResponse;
import com.eventorfront.user.service.UserService;
import com.eventorfront.usergrade.service.GradeService;
import com.eventorfront.userrole.service.UserRoleService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;
	private final AuthService authService;
	private final StatusService statusService;
	private final GradeService gradeService;
	private final UserRoleService userRoleService;

	@GetMapping("/search")
	public ResponseEntity<List<GetUserByIdentifier>> searchUserByIdentifier(@RequestParam String keyword) {
		return ResponseEntity.ok(userService.searchUserByIdentifier(keyword).getData());
	}

	@GetMapping("/search/userId")
	public ResponseEntity<List<GetUserByUserId>> searchUserByUserId(@RequestParam Long userId) {
		return ResponseEntity.ok(userService.searchUserByUserId(userId).getData());
	}

	@AuthorizeRole("admin")
	@GetMapping
	public String getUsers(@PageableDefault(page = 1, size = 10) Pageable pageable,
		Model model) {
		Page<GetUserListResponse> users = userService.getUsers(pageable).getData();
		model.addAttribute("objects", users);
		PagingModel.pagingProcessing(pageable, model, users, "/users", 10);
		return "user/admin/list";
	}

	@AuthorizeRole("admin")
	@GetMapping("/admin")
	public String adminPage() {
		return "user/admin/main";
	}

	@AuthorizeRole("admin")
	@GetMapping("/{userId}")
	public String getUserForm(Model model, @PathVariable Long userId) {
		model.addAttribute("userId", userId);
		model.addAttribute("user", userService.getUser(userId).getData());
		return "user/get";
	}

	@AuthorizeRole("admin")
	@GetMapping("/{userId}/updateForm")
	public String getUserUpdateForm(Model model, @PathVariable Long userId) {
		model.addAttribute("userId", userId);
		model.addAttribute("user", userService.getUser(userId).getData());
		return "user/admin/updateForm";
	}

	@AuthorizeRole("admin")
	@GetMapping("/{userId}/attributeForm")
	public String updateUserAttributeForm(Model model, @PathVariable Long userId) {
		model.addAttribute("userId", userId);
		model.addAttribute("user", userService.getUser(userId).getData());
		model.addAttribute("statuses", statusService.getStatuses("회원").getData());
		model.addAttribute("grades", gradeService.getGrades().getData());
		model.addAttribute("unassignedRoles", userRoleService.getUnassignedUserRoles(userId).getData());
		model.addAttribute("userRoles", userRoleService.getUserRoles(userId).getData());
		return "user/admin/updateAttributeForm";
	}

	@AuthorizeRole("admin")
	@PutMapping("/{userId}")
	public String updateUserByAdmin(@PathVariable Long userId, @ModelAttribute UpdateUserRequest request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userService.updateUserByAdmin(userId, request).getMessage());
		return "redirect:/users/" + userId;
	}

	@AuthorizeRole("admin")
	@PutMapping("/{userId}/attribute")
	public String updateUserAttributeByAdmin(@PathVariable Long userId,
		@ModelAttribute UpdateUserAttributeRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			userService.updateUserAttributeByAdmin(userId, request).getMessage());
		return "redirect:/users/" + userId;
	}

	@GetMapping("/me")
	public String userPage(Model model) {
		model.addAttribute("user", userService.getUserInfo().getData());
		return "user/me";
	}

	@GetMapping("/me/update")
	public String updateUserForm(Model model) {
		model.addAttribute("user", userService.getUserInfo().getData());
		return "user/update";
	}

	@GetMapping("/me/updatePasswordForm")
	public String updatePasswordForm() {
		return "user/updatePasswordForm";
	}

	@PutMapping("/me")
	public String updateUser(@ModelAttribute UpdateUserRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userService.updateUser(request).getMessage());
		return "redirect:/users/me";
	}

	@DeleteMapping("/me")
	public String withdrawUser(HttpServletResponse response, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userService.withdrawUser().getMessage());
		authService.logout();
		CookieUtil.revokeToken(response, "access-token");
		CookieUtil.revokeToken(response, "refresh-token");
		return "redirect:/auth/login";
	}

	@PostMapping("/me/checkNickname")
	ResponseEntity<String> meCheckNickname(@ModelAttribute CheckNicknameRequest request) {
		return ResponseEntity.ok(userService.meCheckNickname(request).getMessage());
	}

	@PutMapping("/me/password")
	public String modifyPassword(@ModelAttribute ModifyPasswordRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userService.modifyPassword(request).getMessage());
		return "redirect:/users/me";
	}

	@GetMapping("/me/Roles")
	public ResponseEntity<List<String>> getRoles() {
		return ResponseEntity.ok(userService.meRoles().getData());
	}

	@GetMapping("/signup")
	public String signup() {
		return "user/signup";
	}

	@PostMapping("/signup")
	public String signup(@ModelAttribute SignUpRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userService.signup(request).getMessage());
		return "redirect:/auth/login";
	}

	@PostMapping("/signup/checkIdentifier")
	public ResponseEntity<String> checkIdentifier(@ModelAttribute CheckIdentifierRequest request) {
		return ResponseEntity.ok(userService.checkIdentifier(request).getMessage());
	}

	@PostMapping("/signup/checkNickname")
	public ResponseEntity<String> checkNickname(@ModelAttribute CheckNicknameRequest request) {
		return ResponseEntity.ok(userService.checkNickname(request).getMessage());
	}

	@PostMapping("/signup/sendEmail")
	ResponseEntity<ApiResponse<Boolean>> sendEmail(@ModelAttribute SendCodeRequest request) {
		return ResponseEntity.ok(userService.sendEmail(request));
	}

	@PostMapping("/signup/certifyEmail")
	ResponseEntity<ApiResponse<Boolean>> certifyEmail(@ModelAttribute CertifyEmailRequest request) {
		return ResponseEntity.ok(userService.certifyEmail(request));
	}

	@GetMapping("/recover/identifier")
	public String recoverIdentifierPage(@RequestParam(required = false) String identifier, Model model) {
		if (identifier != null) {
			model.addAttribute("identifier", identifier);
			model.addAttribute("recoverMessage", userService.recoverIdentifier(identifier).getMessage());
		}
		return "user/recoverIdentifierForm";
	}

	@GetMapping("/recover/password")
	public String recoverPasswordPage(@ModelAttribute CertifyEmailRequest request, Model model) {
		if (request.email() != null) {
			model.addAttribute("recoverMessage", userService.recoverPassword(request.email()).getMessage());
		}
		return "user/recoverPasswordForm";
	}
}