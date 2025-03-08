package com.eventorfront.favorite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;

public interface FavoriteService {

	Page<GetFavoriteResponse> getFavoritesByUserId(Pageable pageable);

	String createOrDeleteFavorite(Long postId);

	String deleteFavorite(Long favoriteId);
}
