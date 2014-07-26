package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;

public class GeoUtils {

	/**
	 * Calculate the angle between vector (first->second) and
	 * vector(second->third). This method assume points are close and thus they
	 * can be seen on the same plane.
	 * 
	 * @param first
	 * @param second
	 * @param third
	 * @return cosine of angle value
	 */
	public static BigDecimal angle(GeoPoint first, GeoPoint second,
			GeoPoint third) {
		throw new RuntimeException("Not implemented");
	}
	
	public static GeoRange buildFromCenter(GeoPoint center, BigDecimal distance) {
		throw new RuntimeException("Not implemented");	
	}
}
