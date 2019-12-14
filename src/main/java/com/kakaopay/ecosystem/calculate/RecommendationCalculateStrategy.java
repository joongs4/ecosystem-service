package com.kakaopay.ecosystem.calculate;

public class RecommendationCalculateStrategy implements CalculateStrategy {

	private final double bigWeight = 0.5;
	private final double middleWeight = 0.3;
	private final double smallWeight = 0.2;

	@Override
	public double calculate(int big, int middle, int small) {

		return (bigWeight * big) + (middleWeight * middle) + (smallWeight * small);
	}

}
