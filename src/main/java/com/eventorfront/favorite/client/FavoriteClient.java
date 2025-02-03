package com.eventorfront.favorite.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;

@FeignClient(name = "favorite-client", url = "http://localhost:8090/back")
public interface FavoriteClient {

	@GetMapping("/users/me/favorites/paging")
	ResponseEntity<Page<GetFavoriteResponse>> getFavoritesByUserId(
		@PageableDefault(page = 1, size = 10) Pageable pageable);

	@PostMapping("/post/{postId}/favorites")
	ResponseEntity<String> createOrDeleteFavorite(@PathVariable Long postId);

	@DeleteMapping("/{favoriteId}")
	ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId);
}
