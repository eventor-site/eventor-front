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

	Page<SearchPostsResponse> searchPosts(Pageable pageable, String keyword);

	Page<GetPostSimpleResponse> getPosts(Pageable pageable);

	List<GetMainPostResponse> getHotEventPosts();

	List<GetMainPostResponse> getLatestEventPosts();

	List<GetMainPostResponse> getDeadlineEventPosts();

	List<GetRecommendPostResponse> getRecommendationEventPosts();

	List<GetRecommendPostResponse> getTrendingEventPosts();

	List<GetMainPostResponse> getHotPostsByCategoryName(String categoryName);

	Page<GetPostsByCategoryNameResponse> getPostsByCategoryName(Pageable pageable, String categoryName);

	Page<GetPostSimpleResponse> getPostsByUserId(Pageable pageable);

	GetPostResponse getPost(Long postId);

	GetTempPostResponse getTempPost();

	ApiResponse<CreatePostResponse> createPost(CreatePostRequest request, boolean isTemp);

	void updatePost(Long postId, UpdatePostRequest request, boolean isTemp);

	String recommendPost(Long postId);

	String disrecommendPost(Long postId);

	void deletePost(Long postId);

	Boolean isAuthorizedToEdit(Long postId);

	void deleteTempPost();
}
