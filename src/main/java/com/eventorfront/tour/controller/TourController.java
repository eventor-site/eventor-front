package com.eventorfront.tour.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.tour.dto.response.GetTourResponse;
import com.eventorfront.tour.dto.response.SearchTourResponse;
import com.eventorfront.tour.service.TourService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tours")
public class TourController {
	private final TourService tourService;

	@GetMapping
	public String getSearchTourForm(Model model) {
		model.addAttribute("radius", "10000");
		return "tour/list";
	}

	@GetMapping("/search")
	public String getTours(@RequestParam String address, @RequestParam(defaultValue = "10000") String radius,
		Model model) {
		SearchTourResponse response = tourService.searchTour(address, radius).getData();
		model.addAttribute("address", address);
		model.addAttribute("radius", radius);
		model.addAttribute("response", response);

		return "tour/list";
	}

	@GetMapping("/{contentId}")
	public String getTour(@PathVariable String contentId, @RequestParam String contentTypeId,
		Model model) {
		GetTourResponse tour = tourService.getTour(contentId, contentTypeId).getData();
		model.addAttribute("tour", tour);
		return "tour/get";
	}

}

