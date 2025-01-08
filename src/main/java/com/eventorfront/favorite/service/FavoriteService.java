package com.eventorfront.favorite.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;

public interface FavoriteService {

	List<GetFavoriteResponse> getFavoritesByUserId();

	ResponseEntity<String> createOrDeleteFavorite(Long postId);

	ResponseEntity<String> deleteFavorite(Long favoriteId);
}
