package com.eventorfront.category.service;

import java.util.List;

import com.sikyeojofront.category.dto.request.CreateCategoryRequest;
import com.sikyeojofront.category.dto.request.UpdateCategoryRequest;
import com.sikyeojofront.category.dto.response.GetCategoryNameResponse;
import com.sikyeojofront.category.dto.response.GetCategoryResponse;

public interface CategoryService {

	List<GetCategoryNameResponse> searchCategories(String keyword);

	List<GetCategoryResponse> getCategories();

	GetCategoryResponse getCategory(Long categoryId);

	void createCategory(CreateCategoryRequest request);

	void updateCategory(Long categoryId, UpdateCategoryRequest request);

	void deleteCategory(Long categoryId);
}
