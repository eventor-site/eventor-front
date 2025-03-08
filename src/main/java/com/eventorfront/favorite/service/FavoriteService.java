package com.eventorfront.favorite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;
import com.eventorfront.global.dto.ApiResponse;

public interface FavoriteService {

	ApiResponse<Page<GetFavoriteResponse>> getFavoritesByUserId(Pageable pageable);

	ApiResponse<Void> createOrDeleteFavorite(Long postId);

	ApiResponse<Void> deleteFavorite(Long favoriteId);
}
