package com.eventorfront.post.service.impl;

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
	public Page<SearchPostsResponse> searchPosts(Pageable pageable, String keyword) {
		return postClient.searchPosts(pageable, keyword).getData();
	}

	@Override
	public Page<GetPostSimpleResponse> getPosts(Pageable pageable) {
		return postClient.getPosts(pageable).getData();
	}

	@Override
	public List<GetMainPostResponse> getHotEventPosts() {
		return postClient.getHotEventPosts().getData();
	}

	@Override
	public List<GetMainPostResponse> getLatestEventPosts() {
		return postClient.getLatestEventPosts().getData();
	}

	@Override
	public List<GetMainPostResponse> getDeadlineEventPosts() {
		return postClient.getDeadlineEventPosts().getData();
	}

	@Override
	public List<GetRecommendPostResponse> getRecommendationEventPosts() {
		return postClient.getRecommendationEventPosts().getData();
	}

	@Override
	public List<GetRecommendPostResponse> getTrendingEventPosts() {
		return postClient.getTrendingEventPosts().getData();
	}

	@Override
	public List<GetMainPostResponse> getHotPostsByCategoryName(String categoryName) {
		return postClient.getHotPostsByCategoryName(categoryName).getData();
	}

	@Override
	public Page<GetPostsByCategoryNameResponse> getPostsByCategoryName(Pageable pageable, String categoryName) {
		return postClient.getPostsByCategoryName(pageable, categoryName).getData();
	}

	@Override
	public Page<GetPostSimpleResponse> getPostsByUserId(Pageable pageable) {
		return postClient.getPostsByUserId(pageable).getData();
	}

	@Override
	public GetPostResponse getPost(Long postId) {
		return postClient.getPost(postId).getData();
	}

	@Override
	public GetTempPostResponse getTempPost() {
		return postClient.getTempPost().getData();
	}

	@Override
	public ApiResponse<CreatePostResponse> createPost(CreatePostRequest request, boolean isTemp) {
		return postClient.createPost(request, isTemp);
	}

	@Override
	public void updatePost(Long postId, UpdatePostRequest request, boolean isTemp) {
		postClient.updatePost(postId, request, isTemp);
	}

	@Override
	public String recommendPost(Long postId) {
		return postClient.recommendPost(postId).getMessage();
	}

	@Override
	public String disrecommendPost(Long postId) {
		return postClient.disrecommendPost(postId).getMessage();
	}

	@Override
	public void deletePost(Long postId) {
		postClient.deletePost(postId);
	}

	@Override
	public Boolean isAuthorizedToEdit(Long postId) {
		return postClient.isAuthorizedToEdit(postId).getData();
	}

	@Override
	public void deleteTempPost() {
		postClient.deleteTempPost();
	}

}
