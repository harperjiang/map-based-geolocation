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
	public static Double angle(GeoPoint first, GeoPoint second, GeoPoint third) {
		Double v1x = second.longitude.doubleValue()
				- first.longitude.doubleValue();
		Double v1y = second.latitude.doubleValue()
				- first.latitude.doubleValue();
		Double v2x = third.longitude.doubleValue()
				- second.longitude.doubleValue();
		Double v2y = third.longitude.doubleValue()
				- second.longitude.doubleValue();

		return (v1x * v2x + v1y * v2y)
				/ (Math.sqrt(v1x * v1x + v1y * v1y) * Math.sqrt(v2x * v2x + v2y
						* v2y));
	}

	/**
	 * Create an square region with center and radius
	 * 
	 * @param center
	 * @param distance
	 * @return
	 */
	public static GeoRange buildFromCenter(GeoPoint center, BigDecimal distance) {
		BigDecimal deltaLat = distanceToLat(distance, center.latitude);
		BigDecimal deltaLong = distanceToLong(distance, center.longitude);

		return new GeoRange(new GeoPoint(center.latitude.subtract(deltaLat),
				center.longitude.subtract(deltaLong)), new GeoDimension(
				deltaLat.add(deltaLat), deltaLong.add(deltaLong)));
	}

	/**
	 * Refer to
	 * http://en.wikipedia.org/wiki/Latitude#Length_of_a_degree_of_latitude
	 * 
	 * @param distance
	 * @param lat
	 * @return
	 */
	public static BigDecimal distanceToLat(BigDecimal distance, BigDecimal lat) {
		BigDecimal factor = new BigDecimal(111132.954 - 559.822
				* Math.cos(Math.toRadians(2 * lat.doubleValue())) + 1.175
				* Math.cos(Math.toRadians(4 * lat.doubleValue())));
		return distance.divide(factor, 10, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal latToDist(BigDecimal lat, BigDecimal delta) {
		BigDecimal factor = new BigDecimal(111132.954 - 559.822
				* Math.cos(Math.toRadians(2 * lat.doubleValue())) + 1.175
				* Math.cos(Math.toRadians(4 * lat.doubleValue())));
		return delta.multiply(factor);
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
				* Math.cos(Math.toRadians(longi.doubleValue())) / 180).abs();
		return distance.divide(factor, 10, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal longToDist(BigDecimal longi, BigDecimal delta) {
		BigDecimal factor = new BigDecimal(Math.PI * 6378137
				* Math.cos(Math.toRadians(longi.doubleValue())) / 180);
		return delta.multiply(factor);
	}

	/**
	 * Calculate the distance between two geo points
	 * 
	 * @param location
	 * @param geoPoint
	 * @return
	 */
	public static BigDecimal distance(GeoPoint a, GeoPoint b) {
		BigDecimal latDist = latToDist(a.latitude,
				a.latitude.subtract(b.latitude)).abs();
		BigDecimal longDist = longToDist(a.longitude,
				a.longitude.subtract(b.longitude)).abs();
		return new BigDecimal(Math.sqrt(latDist.pow(2).doubleValue()
				+ longDist.pow(2).doubleValue()));
	}
}
