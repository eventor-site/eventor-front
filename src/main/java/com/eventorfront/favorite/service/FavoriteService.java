package com.eventorfront.favorite.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.eventorfront.post.dto.response.GetPostSimpleResponse;

public interface FavoriteService {

	List<GetPostSimpleResponse> getFavoritesByUserId();

	ResponseEntity<String> createOrDeleteFavorite(Long postId);

	ResponseEntity<String> deleteFavorite(Long favoriteId);
}
