package com.eventorfront.post.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
import com.eventorfront.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostClient postClient;
	private final ImageClient imageClient;

	@Override
	public List<GetPostSimpleResponse> getPosts() {
		return postClient.getPosts().getBody();
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
	public CreatePostResponse createPost(CreatePostRequest request, List<MultipartFile> files) {
		CreatePostResponse response = postClient.createPost(request).getBody();

		// 파일이 비어 있거나 파일 크기가 0인 파일 필터링
		files = files.stream()
			.filter(file -> file != null && !file.isEmpty() && file.getSize() > 0)
			.toList();

		if (!files.isEmpty()) {
			imageClient.upload(files, "postimage", Objects.requireNonNull(response).postId());
		}
		return response;
	}

	@Override
	public void updatePost(Long postId, UpdatePostRequest request) {
		postClient.updatePost(postId, request);
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
}
