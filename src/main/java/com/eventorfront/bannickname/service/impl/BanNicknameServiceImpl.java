package com.eventorfront.bannickname.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.eventorfront.bannickname.client.BanNicknameClient;
import com.eventorfront.bannickname.dto.BanNicknameDto;
import com.eventorfront.bannickname.service.BanNicknameService;
import com.eventorfront.global.dto.ApiResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BanNicknameServiceImpl implements BanNicknameService {
	private final BanNicknameClient banNicknameClient;

	@Override
	public ApiResponse<List<BanNicknameDto>> getBanNicknames() {
		return banNicknameClient.getBanNicknames().getBody();
	}

	@Override
	public ApiResponse<Page<BanNicknameDto>> getBanNicknames(Pageable pageable) {
		return banNicknameClient.getBanNicknames(pageable).getBody();
	}

	@Override
	public ApiResponse<Void> createBanNickname(BanNicknameDto request) {
		return banNicknameClient.createBanNickname(request).getBody();
	}

	@Override
	public ApiResponse<Void> deleteBanNickname(Long banNicknameId) {
		return banNicknameClient.deleteBanNickname(banNicknameId).getBody();
	}
}
