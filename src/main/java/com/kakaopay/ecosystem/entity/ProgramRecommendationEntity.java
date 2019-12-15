package com.kakaopay.ecosystem.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kakaopay.ecosystem.calculate.CalculateStrategy;
import com.kakaopay.ecosystem.search.SearchStrategy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProgramRecommendationEntity implements Serializable, Comparable<ProgramRecommendationEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -889310346371783930L;
	private String program;
	
	@JsonIgnore
	private String keyword;

	@JsonIgnore
	private EcosystemServiceEntity ecosystemServiceEntity;

	@JsonIgnore
	private double score = 0;

	@JsonIgnore
	private final SearchStrategy searchStrategy;
	@JsonIgnore
	private final CalculateStrategy calculateStrategy;

	public ProgramRecommendationEntity(String keyword, EcosystemServiceEntity ecosystemServiceEntity,
			SearchStrategy searchStrategy, CalculateStrategy calculateStrategy) {
		this.program = ecosystemServiceEntity.getId();
		this.keyword = keyword;
		this.ecosystemServiceEntity = ecosystemServiceEntity;
		this.searchStrategy = searchStrategy;
		this.calculateStrategy = calculateStrategy;

		calculate();
	}

	private double calculate() {

		int themeCount = searchStrategy.countPattern(ecosystemServiceEntity.getTheme(), keyword);
		int introductionCount = searchStrategy.countPattern(ecosystemServiceEntity.getProgramIntroduction(), keyword);
		int detailedIntroductionCount = searchStrategy
				.countPattern(ecosystemServiceEntity.getProgramDetailedIntroduction(), keyword);

		this.score = calculateStrategy.calculate(themeCount, introductionCount, detailedIntroductionCount);

		return score;
	}

	@Override
	public int compareTo(ProgramRecommendationEntity o) {
		// TODO Auto-generated method stub

		Double sourceValue = Double.valueOf(this.score);
		Double targetValue = Double.valueOf(o.getScore());

		return sourceValue.compareTo(targetValue);
	}

}
