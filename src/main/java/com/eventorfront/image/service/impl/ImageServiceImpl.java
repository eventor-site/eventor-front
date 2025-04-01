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

	@Override
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

	// 업로드 이미지 높이가 길면 자동으로 분할 하는 기능
	// public ApiResponse<List<GetImageResponse>> improveUpload(MultipartFile file, Long postId, String categoryName,
	// 	boolean isThumbnail, boolean isPasted) {
	// 	List<MultipartFile> files = convertToWebpAndSplit(file);
	// 	ApiResponse<List<GetImageResponse>> lastResponse = null;
	//
	// 	for (MultipartFile newFile : files) {
	// 		lastResponse = imageClient.upload(newFile, "postimage", postId, categoryName, isThumbnail, isPasted)
	// 			.getBody();
	// 	}
	//
	// 	return lastResponse;
	//
	// }

	// public List<MultipartFile> convertToWebpAndSplit(MultipartFile file) {
	// 	try {
	// 		if (file == null || file.isEmpty() || file.getOriginalFilename() == null) {
	// 			throw new ImageConvertException();
	// 		}
	//
	// 		String originalFilename = file.getOriginalFilename();
	// 		String contentType = file.getContentType();
	//
	// 		// 동영상 파일은 변환 없이 그대로 반환
	// 		if (contentType != null && contentType.startsWith("video/")) {
	// 			return List.of(file);
	// 		}
	//
	// 		if (originalFilename.endsWith(".webp")) {
	// 			return List.of(file);
	// 		}
	//
	// 		ImmutableImage image = ImmutableImage.loader().fromStream(file.getInputStream());
	// 		int width = image.width;
	// 		int height = image.height;
	// 		int maxSegmentHeight = 16000; // 한 조각당 최대 높이 설정
	// 		List<MultipartFile> webpFiles = new ArrayList<>();
	//
	// 		// 이미지가 maxSegmentHeight보다 크면 분할
	// 		if (height > maxSegmentHeight) {
	// 			int segmentCount = (int)Math.ceil((double)height / maxSegmentHeight);
	//
	// 			for (int i = 0; i < segmentCount; i++) {
	// 				int startY = i * maxSegmentHeight;
	// 				int segmentHeight = Math.min(maxSegmentHeight, height - startY);
	//
	// 				ImmutableImage segment = image.subimage(0, startY, width, segmentHeight);
	// 				File tempFile = File.createTempFile("segment_" + i, ".webp");
	// 				segment.output(WebpWriter.DEFAULT, tempFile);
	//
	// 				ByteArrayInputStream webpData = new ByteArrayInputStream(Files.readAllBytes(tempFile.toPath()));
	// 				tempFile.delete();
	//
	// 				webpFiles.add(new CustomMultipartFile(
	// 					webpData,
	// 					originalFilename.substring(0, originalFilename.lastIndexOf('.')) + "_part" + i + ".webp",
	// 					"image/webp"
	// 				));
	// 			}
	// 		} else {
	// 			// 분할할 필요 없으면 기존 방식으로 변환
	// 			File tempFile = new File(originalFilename);
	// 			image.output(WebpWriter.DEFAULT, tempFile);
	//
	// 			ByteArrayInputStream webpData = new ByteArrayInputStream(Files.readAllBytes(tempFile.toPath()));
	// 			tempFile.delete();
	//
	// 			webpFiles.add(new CustomMultipartFile(
	// 				webpData,
	// 				originalFilename.substring(0, originalFilename.lastIndexOf('.')) + ".webp",
	// 				"image/webp"
	// 			));
	// 		}
	// 		return webpFiles;
	// 	} catch (IOException e) {
	// 		throw new ImageConvertException();
	// 	}
	// }

}
