package com.eventorfront.post.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.post.dto.request.CreatePostRequest;
import com.eventorfront.post.dto.request.UpdatePostRequest;
import com.eventorfront.post.dto.response.CreatePostResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.post.dto.response.GetPostResponse;
import com.eventorfront.post.dto.response.GetPostSimpleResponse;
import com.eventorfront.post.dto.response.GetPostsByCategoryNameResponse;

public interface PostService {

	List<GetPostSimpleResponse> getPosts();

	List<GetMainPostResponse> getHotEventPosts();

	List<GetMainPostResponse> getLatestEventPosts();

	List<GetMainPostResponse> getRecommendationEventPosts();

	GetPostsByCategoryNameResponse getPostsByCategoryName(String categoryName);

	GetPostResponse getPost(Long postId);

	CreatePostResponse createPost(CreatePostRequest request, List<MultipartFile> files);

	void updatePost(Long postId, UpdatePostRequest request);

	void recommendPost(Long postId);

	void disrecommendPost(Long postId);

	void deletePost(Long postId);
}
