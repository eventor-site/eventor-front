package com.eventorfront.role.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.role.dto.RoleDto;
import com.eventorfront.role.service.RoleService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
	private final RoleService roleService;
	private static final String REDIRECT_URL = "redirect:/roles";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createRoleForm() {
		return "role/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/{roleId}/update")
	public String updateRoleForm(@PathVariable Long roleId, Model model) {
		model.addAttribute("role", roleService.getRole(roleId).getData());
		return "role/update";
	}

	@GetMapping
	public String getRoles(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<RoleDto> roles = roleService.getRoles(pageable).getData();
		model.addAttribute("objects", roles);
		PagingModel.pagingProcessing(pageable, model, roles, "/roles", 10);
		return "role/list";
	}

	@PostMapping
	public String createRole(@ModelAttribute RoleDto request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", roleService.createRole(request).getMessage());
		return REDIRECT_URL;
	}

	@PutMapping("/{roleId}")
	public String updateRole(@PathVariable Long roleId, @ModelAttribute RoleDto request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", roleService.updateRole(roleId, request).getMessage());
		return REDIRECT_URL;
	}

	@DeleteMapping("/{roleId}")
	public String deleteRole(@PathVariable Long roleId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", roleService.deleteRole(roleId).getMessage());
		return REDIRECT_URL;
	}
}