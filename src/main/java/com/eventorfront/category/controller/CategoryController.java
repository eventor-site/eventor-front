package com.eventorfront.category.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
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

import com.sikyeojofront.category.dto.request.CreateCategoryRequest;
import com.sikyeojofront.category.dto.request.UpdateCategoryRequest;
import com.sikyeojofront.category.dto.response.GetCategoryNameResponse;
import com.sikyeojofront.category.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService categoryService;
	private static final String REDIRECT_URL = "redirect:/categories";

	@GetMapping("/create")
	public String createCategoryForm() {
		return "category/create";
	}

	@GetMapping("/update/{categoryId}")
	public String updateCategoryForm(@PathVariable Long categoryId, Model model) {
		model.addAttribute("category", categoryService.getCategory(categoryId));
		return "category/update";
	}

	@GetMapping("/search")
	public ResponseEntity<List<GetCategoryNameResponse>> searchCategories(@RequestParam String keyword) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.searchCategories(keyword));
	}

	@GetMapping
	public String getCategories(Model model) {
		model.addAttribute("categories", categoryService.getCategories());
		return "category/list";
	}

	@PostMapping
	public String createCategory(@ModelAttribute CreateCategoryRequest request) {
		categoryService.createCategory(request);
		return REDIRECT_URL;
	}

	@PutMapping("/{categoryId}")
	public String updateCategory(@PathVariable Long categoryId, @ModelAttribute UpdateCategoryRequest request) {
		categoryService.updateCategory(categoryId, request);
		return REDIRECT_URL;
	}

	@DeleteMapping("/{categoryId}")
	public String deleteCategory(@PathVariable Long categoryId) {
		categoryService.deleteCategory(categoryId);
		return REDIRECT_URL;
	}
}