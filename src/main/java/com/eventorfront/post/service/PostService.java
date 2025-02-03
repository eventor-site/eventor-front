package com.eventorfront.post.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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

	Page<GetPostSimpleResponse> getPosts(Pageable pageable);

	List<GetMainPostResponse> getHotEventPosts();

	List<GetMainPostResponse> getLatestEventPosts();

	List<GetMainPostResponse> getRecommendationEventPosts();

	List<GetMainPostResponse> getHotPostsByCategoryName(String categoryName);

	List<GetPostsByCategoryNameResponse> getPostsByCategoryName(String categoryName);

	List<GetPostSimpleResponse> getPostsByUserId();

	GetPostResponse getPost(Long postId);

	CreatePostResponse createPost(CreatePostRequest request, List<MultipartFile> files);

	void updatePost(Long postId, UpdatePostRequest request);

	ResponseEntity<String> recommendPost(Long postId);

	ResponseEntity<String> disrecommendPost(Long postId);

	void deletePost(Long postId);
}
