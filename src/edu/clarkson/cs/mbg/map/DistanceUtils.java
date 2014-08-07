package edu.clarkson.cs.mbg.map;

import java.math.BigDecimal;

public class DistanceUtils {

	// 130 km per ms
//	private static final BigDecimal upperFactor = new BigDecimal("130000");

	private static final BigDecimal upperFactor = new BigDecimal("100000");

	// 20 km per ms
	private static final BigDecimal lowerFactor = new BigDecimal("30000");
//	private static final BigDecimal lowerFactor = new BigDecimal("2000");

	/**
	 * From delay(millisecond) to distance(meter),
	 * 
	 * @param input
	 * @return
	 */

	public static BigDecimal upperbound(BigDecimal input) {
		return input.multiply(upperFactor);
	}
	
	public static BigDecimal lowerbound(BigDecimal input) {
		return input.multiply(lowerFactor);
	}
}
