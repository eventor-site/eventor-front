package com.eventorfront.postview.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;
import com.eventorfront.postview.client.PostViewClient;
import com.eventorfront.postview.service.PostVIewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostViewServiceImpl implements PostVIewService {
	private final PostViewClient postViewClient;

	@Override
	public ApiResponse<Page<GetMainPostResponse>> getPostViewsByViewerId(String uuid, Pageable pageable) {
		return postViewClient.getPostViewsByViewerId(uuid, pageable).getBody();
	}

	@Override
	public ApiResponse<Void> deletePostView(Long postViewId) {
		return postViewClient.deletePostView(postViewId).getBody();
	}
}
