package edu.clarkson.cs.mbg.map;

import java.math.BigDecimal;

public class DistanceUtils {

	private static final BigDecimal factor = BigDecimal.TEN;

	public static BigDecimal timeToDistance(BigDecimal input) {
		return input.multiply(factor);
	}
}
