package com.eventorfront.status.controller;

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
import com.eventorfront.status.dto.request.StatusRequest;
import com.eventorfront.status.dto.response.GetStatusResponse;
import com.eventorfront.status.service.StatusService;
import com.eventorfront.statustype.service.StatusTypeService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/statuses")
public class StatusController {
	private final StatusService statusService;
	private final StatusTypeService statusTypeService;
	private static final String REDIRECT_URL = "redirect:/statuses";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createStatusForm(Model model) {
		model.addAttribute("statusTypes", statusTypeService.getStatusTypes().getData());
		return "status/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/update/{statusId}")
	public String updateStatusForm(@PathVariable Long statusId, Model model) {
		model.addAttribute("status", statusService.getStatus(statusId).getData());
		model.addAttribute("statusTypes", statusTypeService.getStatusTypes().getData());
		return "status/update";
	}

	@GetMapping
	public String getStatuses(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetStatusResponse> statuses = statusService.getStatuses(pageable).getData();
		model.addAttribute("objects", statuses);
		PagingModel.pagingProcessing(pageable, model, statuses, "/statuses", 10);
		return "status/list";
	}

	@PostMapping
	public String createStatus(@ModelAttribute StatusRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", statusService.createStatus(request).getMessage());
		return REDIRECT_URL;
	}

	@PutMapping("/{statusId}")
	public String updateStatus(@PathVariable Long statusId, @ModelAttribute StatusRequest request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", statusService.updateStatus(statusId, request).getMessage());
		return REDIRECT_URL;
	}

	@DeleteMapping("/{statusId}")
	public String deleteStatus(@PathVariable Long statusId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", statusService.deleteStatus(statusId).getMessage());
		return REDIRECT_URL;
	}
}