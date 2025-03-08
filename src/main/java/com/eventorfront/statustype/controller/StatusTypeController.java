package com.eventorfront.statustype.controller;

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
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.statustype.dto.StatusTypeDto;
import com.eventorfront.statustype.service.StatusTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/statusTypes")
public class StatusTypeController {
	private final StatusTypeService statusTypeService;
	private static final String REDIRECT_URL = "redirect:/statusTypes";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createStatusTypeForm() {
		return "statusType/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/update/{statusTypeId}")
	public String updateStatusTypeForm(@PathVariable Long statusTypeId, Model model) {
		model.addAttribute("statusType", statusTypeService.getStatusType(statusTypeId).getData());
		return "statusType/update";
	}

	@GetMapping("/search")
	public ResponseEntity<List<StatusTypeDto>> searchStatusTypes(@RequestParam String keyword) {
		return ResponseEntity.ok(statusTypeService.searchStatusTypes(keyword).getData());
	}

	@GetMapping
	public String getStatusTypes(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<StatusTypeDto> statusTypes = statusTypeService.getStatusTypes(pageable).getData();
		model.addAttribute("objects", statusTypes);
		PagingModel.pagingProcessing(pageable, model, statusTypes, "/statusTypes", 10);
		return "statusType/list";
	}

	@PostMapping
	public String createStatusType(@ModelAttribute StatusTypeDto request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", statusTypeService.createStatusType(request).getMessage());
		return REDIRECT_URL;
	}

	@PutMapping("/{statusTypeId}")
	public String updateStatusType(@PathVariable Long statusTypeId,
		@Valid @ModelAttribute StatusTypeDto request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			statusTypeService.updateStatusType(statusTypeId, request).getMessage());
		return REDIRECT_URL;
	}

	@DeleteMapping("/{statusTypeId}")
	public String deleteStatusType(@PathVariable Long statusTypeId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", statusTypeService.deleteStatusType(statusTypeId).getMessage());
		return REDIRECT_URL;
	}
}
