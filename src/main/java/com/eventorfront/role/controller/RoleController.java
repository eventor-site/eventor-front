package com.eventorfront.role.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eventorfront.role.dto.RoleDto;
import com.eventorfront.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
	private final RoleService roleService;
	private static final String REDIRECT_URL = "redirect:/roles";

	@GetMapping("/create")
	public String createRoleForm() {
		return "role/create";
	}

	@GetMapping("/{roleId}/update")
	public String updateRoleForm(@PathVariable Long roleId, Model model) {
		model.addAttribute("role", roleService.getRole(roleId));
		return "role/update";
	}

	@GetMapping
	public String getRoles(Model model) {
		model.addAttribute("roles", roleService.getRoles());
		return "role/list";
	}

	@PostMapping
	public String createRole(@ModelAttribute RoleDto request) {
		roleService.createRole(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{roleId}")
	public String updateRole(@PathVariable Long roleId, @ModelAttribute RoleDto request) {
		roleService.updateRole(roleId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{roleId}")
	public String deleteRole(@PathVariable Long roleId) {
		roleService.deleteRole(roleId);
		return REDIRECT_URL;
	}
}