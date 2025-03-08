package com.eventorfront.category.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.category.dto.request.CreateCategoryRequest;
import com.eventorfront.category.dto.request.UpdateCategoryRequest;
import com.eventorfront.category.dto.response.GetCategoryListResponse;
import com.eventorfront.category.dto.response.GetCategoryNameResponse;
import com.eventorfront.category.dto.response.GetCategoryResponse;

public interface CategoryService {

	List<GetCategoryNameResponse> searchCategories(String keyword);

	List<String> getCategories(String categoryName);

	Page<GetCategoryListResponse> getCategories(Pageable pageable);

	GetCategoryResponse getCategory(Long categoryId);

	String createCategory(CreateCategoryRequest request);

	String updateCategory(Long categoryId, UpdateCategoryRequest request);

	String deleteCategory(Long categoryId);
}
