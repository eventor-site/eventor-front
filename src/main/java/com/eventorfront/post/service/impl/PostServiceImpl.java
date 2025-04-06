package com.eventorfront.post.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.image.client.ImageClient;
import com.eventorfront.post.client.PostClient;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetEventPostCountByAdminResponse;
import com.eventorfront.post.dto.response.GetMainHotPostResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.dto.response.GetPostsByCategoryNameResponse;
import com.eventorfront.post.dto.response.GetRecommendPostResponse;
import com.eventorfront.post.dto.response.GetTempPostResponse;
import com.eventorfront.post.service.PostService;
import com.eventorfront.search.dto.response.SearchPostsResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostClient postClient;
	private final ImageClient imageClient;

	@Override
	public ApiResponse<Page<SearchPostsResponse>> searchPosts(Pageable pageable, String keyword, String categoryName,
		String eventStatusName, String endType) {
		return postClient.searchPosts(pageable, keyword, categoryName, eventStatusName, endType).getBody();
	}

	@Override
	public ApiResponse<Page<GetPostSimpleResponse>> getPosts(Pageable pageable) {
		return postClient.getPosts(pageable).getBody();
	}

	@Override
	public ApiResponse<Page<GetPostSimpleResponse>> monitorPosts(Pageable pageable) {
		return postClient.monitorPosts(pageable).getBody();
	}

	@Override
	public ApiResponse<List<GetMainHotPostResponse>> getHotEventPosts() {
		return postClient.getHotEventPosts().getBody();
	}

	@Override
	public ApiResponse<List<GetMainPostResponse>> getLatestEventPosts() {
		return postClient.getLatestEventPosts().getBody();
	}

	@Override
	public ApiResponse<List<GetMainPostResponse>> getDeadlineEventPosts() {
		return postClient.getDeadlineEventPosts().getBody();
	}

	@Override
	public ApiResponse<List<GetRecommendPostResponse>> getRecommendationEventPosts() {
		return postClient.getRecommendationEventPosts().getBody();
	}

	@Override
	public ApiResponse<List<GetRecommendPostResponse>> getTrendingEventPosts() {
		return postClient.getTrendingEventPosts().getBody();
	}

	@Override
	public ApiResponse<List<GetMainPostResponse>> getHotPostsByCategoryName(String categoryName) {
		return postClient.getHotPostsByCategoryName(categoryName).getBody();
	}

	@Override
	public ApiResponse<Page<GetPostsByCategoryNameResponse>> getPostsByCategoryName(Pageable pageable,
		String categoryName, String eventStatusName, String endType) {
		return postClient.getPostsByCategoryName(pageable, categoryName, eventStatusName, endType).getBody();
	}

	@Override
	public ApiResponse<Page<GetPostSimpleResponse>> getPostsByUserId(Pageable pageable) {
		return postClient.getPostsByUserId(pageable).getBody();
	}

	@Override
	public ApiResponse<GetPostResponse> getPost(Long postId) {
		return postClient.getPost(postId).getBody();
	}

	@Override
	public ApiResponse<GetTempPostResponse> getTempPost() {
		return postClient.getTempPost().getBody();
	}

	@Override
	public ApiResponse<CreatePostResponse> createPost(CreatePostRequest request, boolean isTemp) {
		return postClient.createPost(request, isTemp).getBody();
	}

	@Override
	public ApiResponse<Void> updatePost(Long postId, UpdatePostRequest request, boolean isTemp) {
		return postClient.updatePost(postId, request, isTemp).getBody();
	}

	@Override
	public ApiResponse<Void> finishEventPost(Long postId) {
		return postClient.finishEventPost(postId).getBody();
	}

	@Override
	public ApiResponse<Void> recommendPost(Long postId) {
		return postClient.recommendPost(postId).getBody();
	}

	@Override
	public ApiResponse<Void> disrecommendPost(Long postId) {
		return postClient.disrecommendPost(postId).getBody();
	}

	@Override
	public ApiResponse<Void> deletePost(Long postId) {
		return postClient.deletePost(postId).getBody();
	}

	@Override
	public ApiResponse<Boolean> isAuthorizedToEdit(Long postId) {
		return postClient.isAuthorizedToEdit(postId).getBody();
	}

	@Override
	public void deleteTempPost() {
		postClient.deleteTempPost();
	}

	@Override
	public ApiResponse<List<GetEventPostCountByAdminResponse>> getEventPostCountByAdmin(
		LocalDateTime startTime, LocalDateTime endTime) {
		return postClient.getEventPostCountByAdmin(startTime, endTime).getBody();
	}

}
