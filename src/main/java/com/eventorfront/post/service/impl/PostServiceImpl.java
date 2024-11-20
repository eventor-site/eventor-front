package com.eventorfront.post.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eventorfront.post.client.PostClient;
import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.service.PostService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
	private final PostClient postClient;

	@Override
	public List<GetPostSimpleResponse> getPosts() {
		return postClient.getPosts().getBody();
	}

	@Override
	public List<GetPostSimpleResponse> getPostsByCategoryName(String categoryName) {
		return postClient.getPostsByCategoryName(categoryName).getBody();
	}

	@Override
	public GetPostResponse getPost(Long postId) {
		return postClient.getPost(postId).getBody();
	}

	@Override
	public CreatePostResponse createPost(CreatePostRequest request) {
		return postClient.createPost(request).getBody();
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
