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

		BigDecimal deltaLat = distanceToLat(distance, center.latitude);
		BigDecimal deltaLong = distanceToLong(distance, center.longitude);

		return new GeoRange(new GeoPoint(center.latitude.subtract(deltaLat),
				center.longitude.subtract(deltaLong)), new GeoDimension(
				deltaLat.add(deltaLat), deltaLong.add(deltaLong)));
	}

	public static BigDecimal distanceToLat(BigDecimal distance, BigDecimal lat) {
		BigDecimal factor = new BigDecimal(111132.954 - 559.822
				* Math.cos(Math.toRadians(2 * lat.doubleValue())) + 1.175
				* Math.cos(Math.toRadians(4 * lat.doubleValue())));
		return distance.divide(factor, 10, BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Refer to
	 * http://en.wikipedia.org/wiki/Longitude#Length_of_a_degree_of_longitude
	 * 
	 * @param distance
	 * @param longi
	 * @return
	 */
	public static BigDecimal distanceToLong(BigDecimal distance,
			BigDecimal longi) {
		BigDecimal factor = new BigDecimal(Math.PI * 6378137
				* Math.cos(Math.toRadians(longi.doubleValue())) / 180);
		return distance.divide(factor, 10, BigDecimal.ROUND_HALF_UP);
	}
}
