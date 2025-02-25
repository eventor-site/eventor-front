package com.eventorfront.post.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
		return postClient.searchPosts(pageable, keyword).getBody();
	}

	@Override
	public Page<GetPostSimpleResponse> getPosts(Pageable pageable) {
		return postClient.getPosts(pageable).getBody();
	}

	@Override
	public List<GetMainPostResponse> getHotEventPosts() {
		return postClient.getHotEventPosts().getBody();
	}

	@Override
	public List<GetMainPostResponse> getLatestEventPosts() {
		return postClient.getLatestEventPosts().getBody();
	}

	@Override
	public List<GetRecommendPostResponse> getRecommendationEventPosts() {
		return postClient.getRecommendationEventPosts().getBody();
	}

	@Override
	public List<GetRecommendPostResponse> getTrendingEventPosts() {
		return postClient.getTrendingEventPosts().getBody();
	}

	@Override
	public List<GetMainPostResponse> getHotPostsByCategoryName(String categoryName) {
		return postClient.getHotPostsByCategoryName(categoryName).getBody();
	}

	@Override
	public Page<GetPostsByCategoryNameResponse> getPostsByCategoryName(Pageable pageable, String categoryName) {
		return postClient.getPostsByCategoryName(pageable, categoryName).getData();
	}

	@Override
	public Page<GetPostSimpleResponse> getPostsByUserId(Pageable pageable) {
		return postClient.getPostsByUserId(pageable).getBody();
	}

	@Override
	public GetPostResponse getPost(Long postId) {
		return postClient.getPost(postId).getBody();
	}

	@Override
	public GetTempPostResponse getTempPost() {
		return postClient.getTempPost().getBody();
	}

	@Override
	public CreatePostResponse createPost(CreatePostRequest request, boolean isTemp) {
		return postClient.createPost(request, isTemp).getBody();
	}

	@Override
	public ResponseEntity<Void> updatePost(Long postId, UpdatePostRequest request, boolean isTemp) {
		return postClient.updatePost(postId, request, isTemp);
	}

	@Override
	public ResponseEntity<String> recommendPost(Long postId) {
		return postClient.recommendPost(postId);
	}

	@Override
	public ResponseEntity<String> disrecommendPost(Long postId) {
		return postClient.disrecommendPost(postId);
	}

	@Override
	public void deletePost(Long postId) {
		postClient.deletePost(postId);
	}

	@Override
	public Boolean isAuthorizedToEdit(Long postId) {
		return postClient.isAuthorizedToEdit(postId).getBody();
	}

	@Override
	public ResponseEntity<Void> deleteTempPost() {
		return postClient.deleteTempPost();
	}

}
