package org.mainapplication.domain.post.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.domainmodule.postgroup.entity.type.PostGroupPurposeType;
import org.domainmodule.postgroup.entity.type.PostGroupReferenceType;
import org.domainmodule.postgroup.entity.type.PostLengthType;
import org.domainmodule.rssfeed.entity.type.FeedCategoryType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mainapplication.domain.post.controller.request.CreatePostsRequest;
import org.mainapplication.domain.post.controller.response.CreatePostsResponse;
import org.openaiclient.client.OpenAiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostServiceTest {

	@Autowired
	PostService postService;
	@Autowired
	OpenAiClient openAiClient;

	@Test
	@Transactional
	void createPostsWithoutRef() {
		// Given
		CreatePostsRequest request = new CreatePostsRequest(
			"오늘의 점심 또는 저녁 메뉴",
			PostGroupPurposeType.OPINION,
			PostGroupReferenceType.NONE,
			null,
			null,
			PostLengthType.SHORT,
			"오늘 이 메뉴는 어떨까나~"
		);

		// When
		CreatePostsResponse response = postService.createPostsWithoutRef(request, 5);

		// Then
		Assertions.assertAll(
			() -> Assertions.assertNotNull(response.getPostGroupId()),
			() -> Assertions.assertNull(response.getEof()),
			() -> Assertions.assertEquals(5, response.getPosts().size())
		);
	}

	@Test
	@Transactional
	void createPostsByNews() {
		// Given
		CreatePostsRequest request = new CreatePostsRequest(
			"주식 관련 소식 알아보기",
			PostGroupPurposeType.INFORMATION,
			PostGroupReferenceType.NEWS,
			FeedCategoryType.INVEST,
			null,
			PostLengthType.SHORT,
			"'화성 가즈아~~'와 같은 추임새를 포함하기"
		);

		// When
		CreatePostsResponse response = postService.createPostsByNews(request, 5);

		// Then
		Assertions.assertAll(
			() -> Assertions.assertNotNull(response.getPostGroupId()),
			() -> Assertions.assertFalse(response.getEof()),
			() -> Assertions.assertEquals(5, response.getPosts().size())
		);
	}

	@Test
	@Transactional
	void createPostsByImage() throws IOException {
		// Given
		Path filePath = Path.of("src/test/resources/tanqueray.jpeg");
		String fileName = filePath.getFileName().toString();
		byte[] fileContent = Files.readAllBytes(filePath);
		MockMultipartFile mockFile = new MockMultipartFile("file", fileName, "image/jpeg", fileContent);
		CreatePostsRequest request = new CreatePostsRequest(
			"우리 브랜드 제품 홍보",
			PostGroupPurposeType.MARKETING,
			PostGroupReferenceType.IMAGE,
			null,
			List.of(mockFile),
			PostLengthType.SHORT,
			"텡커레이 no.10의 시트러스한 상큼함과 깔끔함을 강조하는 홍보 멘트, '좋은 시간 좋은 술.'과 같은 느끼한 마무리 멘트"
		);

		// When
		CreatePostsResponse response = postService.createPostsByImage(request, 5);

		// Then
		Assertions.assertAll(
			() -> Assertions.assertNotNull(response.getPostGroupId()),
			() -> Assertions.assertNull(response.getEof()),
			() -> Assertions.assertEquals(5, response.getPosts().size())
		);
	}
}
