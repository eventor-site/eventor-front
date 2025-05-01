package com.eventorfront.bannickname.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.eventorfront.auth.annotation.AuthorizeRole;
import com.eventorfront.bannickname.dto.BanNicknameDto;
import com.eventorfront.bannickname.service.BanNicknameService;
import com.eventorfront.global.util.PagingModel;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/banNicknames")
public class BanNicknameController {
	private final BanNicknameService banNicknameService;
	private static final String REDIRECT_URL = "redirect:/banNicknames";

	@AuthorizeRole("admin")
	@GetMapping("/create")
	public String createBanNicknameForm() {
		return "banNickname/create";
	}

	@GetMapping
	public String getBanNicknames(Model model) {
		List<BanNicknameDto> banNicknames = banNicknameService.getBanNicknames().getData();
		model.addAttribute("banNicknames", banNicknames);
		return "banNickname/list";
	}

	@GetMapping("/paging")
	public String getBanNicknames(@PageableDefault(page = 1, size = 10) Pageable pageable, Model model) {
		Page<BanNicknameDto> banNicknames = banNicknameService.getBanNicknames(pageable).getData();
		model.addAttribute("objects", banNicknames);
		PagingModel.pagingProcessing(pageable, model, banNicknames, "/banNicknames", 10);
		return "banNickname/list";
	}

	@PostMapping
	public String createBanNickname(@ModelAttribute BanNicknameDto request, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", banNicknameService.createBanNickname(request).getMessage());
		return REDIRECT_URL;
	}

	@DeleteMapping("/{banNicknameId}")
	public String deleteBanNickname(@PathVariable Long banNicknameId, RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message",
			banNicknameService.deleteBanNickname(banNicknameId).getMessage());
		return REDIRECT_URL;
	}
}
