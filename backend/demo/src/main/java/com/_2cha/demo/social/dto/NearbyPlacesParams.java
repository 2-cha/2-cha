package com._2cha.demo.social.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(SnakeCaseStrategy.class)
public class NearbyPlacesParams {

	private Double let;
	private Double lon;
	private Double maxDist;
	private List<Long> filteredTagIds;

	public NearbyPlacesParams(Double let, Double lon, Double maxDist, List<Long> filteredTagIds) {
		this.let = let;
		this.lon = lon;
		this.maxDist = maxDist;
		this.filteredTagIds = filteredTagIds;
	}
}
