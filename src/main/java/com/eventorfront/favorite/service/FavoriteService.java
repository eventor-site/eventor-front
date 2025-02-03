package com.eventorfront.favorite.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;

public interface FavoriteService {

	Page<GetFavoriteResponse> getFavoritesByUserId(Pageable pageable);

	ResponseEntity<String> createOrDeleteFavorite(Long postId);

	ResponseEntity<String> deleteFavorite(Long favoriteId);
}
