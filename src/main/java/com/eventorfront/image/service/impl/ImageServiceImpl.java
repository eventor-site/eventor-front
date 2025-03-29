package com.eventorfront.image.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eventorfront.global.dto.ApiResponse;
import com.eventorfront.image.client.ImageClient;
import com.eventorfront.image.dto.request.DeleteImageRequest;
import com.eventorfront.image.dto.response.GetImageResponse;
import com.eventorfront.image.exception.ImageConvertException;
import com.eventorfront.image.service.CustomMultipartFile;
import com.eventorfront.image.service.ImageService;
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
	private final ImageClient imageClient;

	@Override
	public ApiResponse<List<GetImageResponse>> upload(MultipartFile file, Long postId, String categoryName,
		boolean isThumbnail, boolean isPasted) {
		return imageClient.upload(convertToWebp(file), "postimage", postId, categoryName, isThumbnail, isPasted)
			.getBody();
	}

	@Override
	public ApiResponse<List<GetImageResponse>> deleteImages(DeleteImageRequest request) {
		return imageClient.deleteImages(request).getBody();
	}

	@Override
	public void deleteTempImage() {
		imageClient.deleteTempImage();
	}

	public MultipartFile convertToWebp(MultipartFile file) {
		try {

			if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
				throw new ImageConvertException();
			}

			String originalFilename = file.getOriginalFilename();
			String contentType = file.getContentType();

			// 동영상 파일은 변환 없이 그대로 반환
			if (contentType != null && contentType.startsWith("video/")) {
				return file;
			}

			if (originalFilename.endsWith(".webp")) {
				return file;
			}

			File tempFile = new File(file.getOriginalFilename());
			ImmutableImage image = ImmutableImage.loader().fromStream(file.getInputStream());

			// WebP로 변환 (손실 압축)
			image.output(WebpWriter.DEFAULT, tempFile);

			// 변환된 데이터를 메모리에 저장 후 임시 파일 삭제
			ByteArrayInputStream webpData = new ByteArrayInputStream(Files.readAllBytes(tempFile.toPath()));
			tempFile.delete(); // 변환 후 즉시 삭제

			return new CustomMultipartFile(
				webpData,
				originalFilename.substring(0, originalFilename.lastIndexOf('.')) + ".webp",
				"image/webp"
			);

		} catch (IOException e) {
			throw new ImageConvertException();
		}
	}

}
