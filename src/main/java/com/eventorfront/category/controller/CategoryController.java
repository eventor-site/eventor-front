package com.eventorfront.category.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.category.dto.request.CreateCategoryRequest;
import com.eventorfront.category.dto.request.UpdateCategoryRequest;
import com.eventorfront.category.dto.response.GetCategoryListResponse;
import com.eventorfront.category.dto.response.GetCategoryNameResponse;
import com.eventorfront.category.service.CategoryService;
import com.eventorfront.global.util.PagingModel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {
	private final CategoryService categoryService;
	private static final String REDIRECT_URL = "redirect:/categories";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createCategoryForm() {
		return "category/create";
	}

	@AuthorizeRole("admin")
	@GetMapping("/update/{categoryId}")
	public String updateCategoryForm(@PathVariable Long categoryId, Model model) {
		model.addAttribute("category", categoryService.getCategory(categoryId).getData());
		return "category/update";
	}

	@AuthorizeRole("admin")
	@GetMapping("/search")
	public ResponseEntity<List<GetCategoryNameResponse>> searchCategories(@RequestParam String keyword) {
		return ResponseEntity.status(HttpStatus.OK).body(categoryService.searchCategories(keyword).getData());
	}

	@AuthorizeRole("admin")
	@GetMapping
	public String getCategories(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetCategoryListResponse> categories = categoryService.getCategories(pageable).getData();
		model.addAttribute("objects", categories);
		PagingModel.pagingProcessing(pageable, model, categories, "/categories", 10);
		return "category/list";
	}

	@PostMapping
	public String createCategory(@ModelAttribute CreateCategoryRequest request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", categoryService.createCategory(request).getMessage());
		return REDIRECT_URL;
	}

	@PutMapping("/{categoryId}")
	public String updateCategory(@PathVariable Long categoryId, @ModelAttribute UpdateCategoryRequest request,
		RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			categoryService.updateCategory(categoryId, request).getMessage());
		return REDIRECT_URL;
	}

	@DeleteMapping("/{categoryId}")
	public String deleteCategory(@PathVariable Long categoryId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", categoryService.deleteCategory(categoryId).getMessage());
		return REDIRECT_URL;
	}
}