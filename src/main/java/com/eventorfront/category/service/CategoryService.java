package com.eventorfront.category.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.category.dto.request.CreateCategoryRequest;
import com.eventorfront.category.dto.request.UpdateCategoryRequest;
import com.eventorfront.category.dto.response.GetCategoryListResponse;
import com.eventorfront.category.dto.response.GetCategoryNameResponse;
import com.eventorfront.category.dto.response.GetCategoryResponse;
import com.eventorfront.global.dto.ApiResponse;

public interface CategoryService {

	ApiResponse<List<GetCategoryNameResponse>> searchCategories(String keyword);

	ApiResponse<List<String>> getCategories(String categoryName);

	ApiResponse<Page<GetCategoryListResponse>> getCategories(Pageable pageable);

	ApiResponse<GetCategoryResponse> getCategory(Long categoryId);

	ApiResponse<Void> createCategory(CreateCategoryRequest request);

	ApiResponse<Void> updateCategory(Long categoryId, UpdateCategoryRequest request);

	ApiResponse<Void> deleteCategory(Long categoryId);
}
