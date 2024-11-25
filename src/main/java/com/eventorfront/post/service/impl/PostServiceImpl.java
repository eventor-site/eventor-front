package com.eventorfront.post.service.impl;

import java.util.List;
import java.util.Objects;

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
	public List<GetMainPostResponse> getHotEventPosts() {
		return postClient.getHotEventPosts().getBody();
	}

	@Override
	public List<GetMainPostResponse> getLatestEventPosts() {
		return postClient.getLatestEventPosts().getBody();
	}

	@Override
	public List<GetMainPostResponse> getRecommendationEventPosts() {
		return postClient.getRecommendationEventPosts().getBody();
	}

	@Override
	public GetPostsByCategoryNameResponse getPostsByCategoryName(String categoryName) {
		return postClient.getPostsByCategoryName(categoryName).getBody();
	}

	@Override
	public GetPostResponse getPost(Long postId) {
		return postClient.getPost(postId).getBody();
	}

	@Override
	public CreatePostResponse createPost(CreatePostRequest request, List<MultipartFile> files) {
		CreatePostResponse response = postClient.createPost(request).getBody();
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
	public void deletePost(Long postId) {
		postClient.deletePost(postId);
	}
}
