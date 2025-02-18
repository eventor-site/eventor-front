package com.eventorfront.category.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.eventorfront.category.dto.request.CreateCategoryRequest;
import com.eventorfront.category.dto.request.UpdateCategoryRequest;
import com.eventorfront.category.dto.response.GetCategoryListResponse;
import com.eventorfront.category.dto.response.GetCategoryNameResponse;
import com.eventorfront.category.dto.response.GetCategoryResponse;

@FeignClient(name = "category-client", url = "${feignClient.url}")
public interface CategoryClient {

	@GetMapping("/back/categories/search")
	ResponseEntity<List<GetCategoryNameResponse>> searchCategories(@RequestParam String keyword);

	@GetMapping("/back/categories/paging")
	ResponseEntity<Page<GetCategoryListResponse>> getCategories(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@GetMapping("/back/categories/{categoryId}")
	ResponseEntity<GetCategoryResponse> getCategory(@PathVariable Long categoryId);

	@PostMapping("/back/categories")
	ResponseEntity<Void> createCategory(@RequestBody CreateCategoryRequest request);

	@PutMapping("/back/categories/{categoryId}")
	ResponseEntity<Void> updateCategory(@PathVariable Long categoryId, @RequestBody UpdateCategoryRequest request);

	@DeleteMapping("/back/categories/{categoryId}")
	ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId);
}
