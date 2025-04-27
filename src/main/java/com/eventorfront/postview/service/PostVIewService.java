package com.eventorfront.postview.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.post.dto.response.GetMainPostResponse;

public interface PostVIewService {

	ApiResponse<Page<GetMainPostResponse>> getPostViewsByViewerId(String uuid, Pageable pageable);

	ApiResponse<Void> deletePostView(Long postViewId);
}
