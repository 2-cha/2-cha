package com._2cha.demo.social.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Auth
@RestController
@RequiredArgsConstructor
public class SocialController {
	private final ReviewService reviewService;

	//리뷰 작성일자 순으로 리뷰 목록을 가져오는 API
	@GetMapping("/social")
	public List<ReviewResponse> getSocialReviews() {
		return reviewService.getSocialReviewsOrderByNewest();
	}


	//특정 태그를 가진 리뷰
	@GetMapping("/social/filteredbytag")
	public List<ReviewResponse> getSocialReviewsByTag(
			@RequestParam(name = "filter_by_tag") List<Long> filterTagsId
	) {
		return reviewService.getSocialReviewsWithTag(filterTagsId);
	}
}
