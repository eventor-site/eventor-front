package com.eventorfront.favorite.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.eventorfront.favorite.dto.response.GetFavoriteResponse;
import com.eventorfront.favorite.service.FavoriteService;
import com.eventorfront.global.util.PagingModel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class FavoriteController {
	private final FavoriteService favoriteService;

	@GetMapping("/users/me/favorites")
	public String getFavoritesByUserId(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetFavoriteResponse> favorites = favoriteService.getFavoritesByUserId(pageable);
		model.addAttribute("objects", favorites);
		PagingModel.pagingProcessing(pageable, model, favorites, "/users/me/favorites", 10);
		return "favorite/list";
	}

	@PostMapping("/posts/{postId}/favorites")
	public ResponseEntity<String> createOrDeleteFavorite(@PathVariable Long postId) {
		return favoriteService.createOrDeleteFavorite(postId);
	}

	@DeleteMapping("/favorites/{favoriteId}")
	public String deleteFavorite(@PathVariable Long favoriteId) {
		favoriteService.deleteFavorite(favoriteId);
		return "redirect:/users/me/favorites";
	}
}
