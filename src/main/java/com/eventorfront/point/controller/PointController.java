package com.eventorfront.point.controller;

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

import com.eventorfront.global.util.PagingModel;
import com.eventorfront.point.dto.request.PointRequest;
import com.eventorfront.point.dto.response.GetPointResponse;
import com.eventorfront.point.service.PointService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/points")
public class PointController {
	private final PointService pointService;
	private static final String REDIRECT_URL = "redirect:/points";

	@GetMapping("/create")
	public String createPointForm() {
		return "point/create";
	}

	@GetMapping("/update/{pointId}")
	public String updatePointForm(@PathVariable Long pointId, Model model) {
		model.addAttribute("point", pointService.getPoint(pointId));
		return "point/update";
	}

	@GetMapping
	public String getPoints(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetPointResponse> points = pointService.getPoints(pageable);
		model.addAttribute("objects", points);
		PagingModel.pagingProcessing(pageable, model, points, "/points", 10);
		return "point/list";
	}

	@PostMapping
	public String createPoint(@ModelAttribute PointRequest request) {
		pointService.createPoint(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{pointId}")
	public String updatePoint(@PathVariable Long pointId, @ModelAttribute PointRequest request) {
		pointService.updatePoint(pointId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{pointId}")
	public String deletePoint(@PathVariable Long pointId) {
		pointService.deletePoint(pointId);
		return REDIRECT_URL;
	}
}