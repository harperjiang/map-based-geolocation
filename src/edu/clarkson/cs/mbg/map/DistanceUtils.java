package edu.clarkson.cs.mbg.map;

import java.math.BigDecimal;

public class DistanceUtils {

	private static final BigDecimal factor = new BigDecimal(10000);

	/**
	 * From delay(millisecond) to distance(meter),
	 * 
	 * @param input
	 * @return
	 */

	public static BigDecimal timeToDistance(BigDecimal input) {
		return input.multiply(factor);
	}
}
