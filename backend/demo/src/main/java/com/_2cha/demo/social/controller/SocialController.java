package com._2cha.demo.social.controller;

import com._2cha.demo.global.annotation.Auth;
import com._2cha.demo.place.dto.FilterBy;
import com._2cha.demo.place.dto.NearbyPlaceSearchParams;
import com._2cha.demo.place.dto.SortBy;
import com._2cha.demo.review.dto.ReviewResponse;
import com._2cha.demo.review.service.ReviewService;
import com._2cha.demo.social.dto.NearbyPlacesParams;
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
		return reviewService.getReviewsOrderByNewest();
	}


	//특정 태그를 가진 리뷰
	@GetMapping("/social/filteredbytag")
	public List<ReviewResponse> getSocialReviewsByTag(
			@RequestParam(name = "filter_by_tag") List<Long> filterTagsId
	) {
		return reviewService.getReviewsWithTag(filterTagsId);
	}

	@GetMapping("/social/nearby")
	public List<ReviewResponse> getReviewsByNearby(
			@RequestParam(name = "filter_by_tag") List<Long> filterTagsId,
			@RequestParam(name = "max_dist") Double maxDist,
			@RequestParam(name = "lat") Double lat ,
			@RequestParam(name = "lon") Double lon
	) {
		NearbyPlaceSearchParams nearbyPlacesParams = new NearbyPlaceSearchParams(lat, lon, maxDist, FilterBy.DEFAULT, null, SortBy.DISTANCE, 0L, 20);
		return reviewService.getReviewsNearbyPlaces(nearbyPlacesParams, filterTagsId);
		//feat
		// 1. 근처 가게의 리뷰, 필터를 받을 수도 있다. 원하는 태그의 리뷰만 볼수 있음
		// 2. 특정 위치를 받아서 해당 위치의 특정범위내 가게들의 리뷰를 가져와야함.
		// 3. 가져온 리뷰는 최신순으로 가져와야됨 페이지네이션? 나중에 생각하자.
	}

}
