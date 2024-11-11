package com.eventorfront.category.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.category.client.CategoryClient;
import com.eventorfront.category.dto.request.CreateCategoryRequest;
import com.eventorfront.category.dto.request.UpdateCategoryRequest;
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
		return categoryClient.searchCategories(keyword).getBody();
	}

	@Override
	public List<GetCategoryResponse> getCategories() {
		return categoryClient.getCategories().getBody();
	}

	@Override
	public GetCategoryResponse getCategory(Long categoryId) {
		return categoryClient.getCategory(categoryId).getBody();
	}

	@Override
	public void createCategory(CreateCategoryRequest request) {
		categoryClient.createCategory(request);
	}

	@Override
	public void updateCategory(Long statusTypeId, UpdateCategoryRequest request) {
		categoryClient.updateCategory(statusTypeId, request);
	}

	@Override
	public void deleteCategory(Long statusTypeId) {
		categoryClient.deleteCategory(statusTypeId);
	}
}
