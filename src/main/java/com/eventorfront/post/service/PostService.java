package com.eventorfront.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.dto.response.GetPostsByCategoryNameResponse;
import com.eventorfront.post.dto.response.GetRecommendPostResponse;
import com.eventorfront.post.dto.response.GetTempPostResponse;
import com.eventorfront.search.dto.response.SearchPostsResponse;

public interface PostService {

	ApiResponse<Page<SearchPostsResponse>> searchPosts(Pageable pageable, String keyword);

	ApiResponse<Page<GetPostSimpleResponse>> getPosts(Pageable pageable);

	ApiResponse<List<GetMainPostResponse>> getHotEventPosts();

	ApiResponse<List<GetMainPostResponse>> getLatestEventPosts();

	ApiResponse<List<GetMainPostResponse>> getDeadlineEventPosts();

	ApiResponse<List<GetRecommendPostResponse>> getRecommendationEventPosts();

	ApiResponse<List<GetRecommendPostResponse>> getTrendingEventPosts();

	ApiResponse<List<GetMainPostResponse>> getHotPostsByCategoryName(String categoryName);

	ApiResponse<Page<GetPostsByCategoryNameResponse>> getPostsByCategoryName(Pageable pageable, String categoryName);

	ApiResponse<Page<GetPostSimpleResponse>> getPostsByUserId(Pageable pageable);

	ApiResponse<GetPostResponse> getPost(Long postId);

	ApiResponse<GetTempPostResponse> getTempPost();

	ApiResponse<CreatePostResponse> createPost(CreatePostRequest request, boolean isTemp);

	ApiResponse<Void> updatePost(Long postId, UpdatePostRequest request, boolean isTemp);

	ApiResponse<Void> recommendPost(Long postId);

	ApiResponse<Void> disrecommendPost(Long postId);

	ApiResponse<Void> deletePost(Long postId);

	ApiResponse<Boolean> isAuthorizedToEdit(Long postId);

	void deleteTempPost();
}
