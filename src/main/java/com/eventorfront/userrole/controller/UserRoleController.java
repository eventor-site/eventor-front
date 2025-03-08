package com.eventorfront.userrole.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.userrole.service.UserRoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserRoleController {
	private final UserRoleService userRoleService;

	@PostMapping("/{userId}/roles")
	public String createUserRole(@PathVariable Long userId, @RequestParam Long roleId,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userRoleService.createUserRole(userId, roleId));
		return "redirect:/users/" + userId + "/attributeForm";
	}

	@DeleteMapping("/{userId}/roles/{roleId}")
	public String deleteUserRole(@PathVariable Long userId, @PathVariable Long roleId,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", userRoleService.deleteUserRole(userId, roleId));
		return "redirect:/users/" + userId + "/attributeForm";
	}
}