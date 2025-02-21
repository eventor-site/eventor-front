package com.eventorfront.userstop.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.global.util.PagingModel;
import com.eventorfront.reporttype.service.ReportTypeService;
import com.eventorfront.userstop.dto.UserStopDto;
import com.eventorfront.userstop.dto.response.GetUserStopByUserIdResponse;
import com.eventorfront.userstop.dto.response.GetUserStopResponse;
import com.eventorfront.userstop.service.UserStopService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/userStops")
public class UserStopController {
	private final UserStopService userStopService;
	private final ReportTypeService reportTypeService;
	private static final String REDIRECT_URL = "redirect:/userStops";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createUserStopForm(Model model) {
		model.addAttribute("reportTypes", reportTypeService.getReportTypes());
		return "userStop/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/update/{userStopId}")
	public String updateUserStopForm(@PathVariable Long userStopId, Model model) {
		model.addAttribute("userStop", userStopService.getUserStop(userStopId));
		return "userStop/update";
	}

	@GetMapping
	public String getUserStops(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetUserStopResponse> userStops = userStopService.getUserStops(pageable);
		model.addAttribute("objects", userStops);
		PagingModel.pagingProcessing(pageable, model, userStops, "/userStops", 10);
		return "userStop/list";
	}

	@GetMapping("/users")
	@ResponseBody
	public List<GetUserStopByUserIdResponse> getUserStopsByUserId(@RequestParam Long userId) {
		return userStopService.getUserStopsByUserId(userId);
	}

	@PostMapping
	public String createUserStop(@ModelAttribute UserStopDto request) {
		userStopService.createUserStop(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{userStopId}")
	public String updateUserStop(@PathVariable Long userStopId, @ModelAttribute UserStopDto request) {
		userStopService.updateUserStop(userStopId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{userStopId}")
	public String deleteUserStop(@PathVariable Long userStopId) {
		userStopService.deleteUserStop(userStopId);
		return REDIRECT_URL;
	}
}