package com.eventorfront.bannickname.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.eventorfront.bannickname.dto.BanNicknameDto;
import com.eventorfront.global.dto.ApiResponse;

public interface BanNicknameService {

	ApiResponse<List<BanNicknameDto>> getBanNicknames();

	ApiResponse<Page<BanNicknameDto>> getBanNicknames(Pageable pageable);

	ApiResponse<Void> createBanNickname(BanNicknameDto request);

	ApiResponse<Void> deleteBanNickname(Long banNicknameId);
}
