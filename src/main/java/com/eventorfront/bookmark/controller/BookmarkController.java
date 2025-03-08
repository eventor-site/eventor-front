package com.eventorfront.bookmark.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.bookmark.dto.response.GetBookmarkResponse;
import com.eventorfront.bookmark.service.BookmarkService;
import com.eventorfront.global.util.PagingModel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BookmarkController {
	private final BookmarkService bookmarkService;

	@GetMapping("/users/me/bookmarks")
	public String getBookmarksByUserId(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<GetBookmarkResponse> bookmarks = bookmarkService.getBookmarksByUserId(pageable);
		model.addAttribute("objects", bookmarks);
		PagingModel.pagingProcessing(pageable, model, bookmarks, "/users/me/bookmarks", 10);
		return "bookmark/list";
	}

	@PostMapping("/categories/{categoryName}/bookmarks")
	public ResponseEntity<String> createOrDeleteBookmark(@PathVariable String categoryName) {
		return ResponseEntity.ok(bookmarkService.createOrDeleteBookmark(categoryName));
	}

	@DeleteMapping("/bookmarks/{bookmarkId}")
	public String deleteBookmark(@PathVariable Long bookmarkId, RedirectAttributes redirectAttributes) {

		redirectAttributes.addFlashAttribute("message", bookmarkService.deleteBookmark(bookmarkId));
		return "redirect:/users/me/bookmarks";
	}
}
