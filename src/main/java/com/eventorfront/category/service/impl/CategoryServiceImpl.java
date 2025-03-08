package com.eventorfront.category.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.category.client.CategoryClient;
import com.eventorfront.category.dto.request.CreateCategoryRequest;
import com.eventorfront.category.dto.request.UpdateCategoryRequest;
import com.eventorfront.category.dto.response.GetCategoryListResponse;
import com.eventorfront.category.dto.response.GetCategoryNameResponse;
import com.eventorfront.category.dto.response.GetCategoryResponse;
import com.eventorfront.category.service.CategoryService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryClient categoryClient;

	@Override
	public ApiResponse<List<GetCategoryNameResponse>> searchCategories(String keyword) {
		return categoryClient.searchCategories(keyword).getBody();
	}

	@Override
	public ApiResponse<List<String>> getCategories(String categoryName) {
		return categoryClient.getCategories(categoryName).getBody();
	}

	@Override
	public ApiResponse<Page<GetCategoryListResponse>> getCategories(Pageable pageable) {
		return categoryClient.getCategories(pageable).getBody();
	}

	@Override
	public ApiResponse<GetCategoryResponse> getCategory(Long categoryId) {
		return categoryClient.getCategory(categoryId).getBody();
	}

	@Override
	public ApiResponse<Void> createCategory(CreateCategoryRequest request) {
		return categoryClient.createCategory(request).getBody();
	}

	@Override
	public ApiResponse<Void> updateCategory(Long statusTypeId, UpdateCategoryRequest request) {
		return categoryClient.updateCategory(statusTypeId, request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteCategory(Long statusTypeId) {
		return categoryClient.deleteCategory(statusTypeId).getBody();
	}
}
