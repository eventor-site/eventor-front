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

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryClient categoryClient;

	@Override
	public List<GetCategoryNameResponse> searchCategories(String keyword) {
		return categoryClient.searchCategories(keyword).getData();
	}

	@Override
	public List<String> getCategories(String categoryName) {
		return categoryClient.getCategories(categoryName).getData();
	}

	@Override
	public Page<GetCategoryListResponse> getCategories(Pageable pageable) {
		return categoryClient.getCategories(pageable).getData();
	}

	@Override
	public GetCategoryResponse getCategory(Long categoryId) {
		return categoryClient.getCategory(categoryId).getData();
	}

	@Override
	public String createCategory(CreateCategoryRequest request) {
		return categoryClient.createCategory(request).getMessage();
	}

	@Override
	public String updateCategory(Long statusTypeId, UpdateCategoryRequest request) {
		return categoryClient.updateCategory(statusTypeId, request).getMessage();
	}

	@Override
	public String deleteCategory(Long statusTypeId) {
		return categoryClient.deleteCategory(statusTypeId).getMessage();
	}
}
