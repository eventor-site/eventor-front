package com.eventorfront.post.service;

import java.util.List;

import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;

public interface PostService {

	List<GetPostSimpleResponse> getPosts();

	List<GetPostSimpleResponse> getPostsByCategoryName(String categoryName);

	GetPostResponse getPost(Long postId);

	CreatePostResponse createPost(CreatePostRequest request);

	void updatePost(Long postId, UpdatePostRequest request);

	void deletePost(Long postId);
}
