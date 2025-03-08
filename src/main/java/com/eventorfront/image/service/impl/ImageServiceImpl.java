package com.eventorfront.image.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.image.client.ImageClient;
import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;
import com.eventorfront.image.service.ImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	private final ImageClient imageClient;

	@Override
	public List<GetImageResponse> upload(MultipartFile file, Long postId, boolean isThumbnail,
		boolean isPasted) {
		return imageClient.upload(file, "postimage", postId, isThumbnail, isPasted).getData();
	}

	@Override
	public List<GetImageResponse> deleteImages(DeleteImageRequest request) {
		return imageClient.deleteImages(request).getData();
	}

	@Override
	public void deleteTempImage() {
		imageClient.deleteTempImage();
	}
}
