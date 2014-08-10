package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;

public class GeoUtils {

	static final BigDecimal TWO = new BigDecimal("2");

	static final int PRECISION = 5;

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
	public static Double angleInCos(GeoPoint first, GeoPoint second,
			GeoPoint third, GeoPoint forth) {
		Double v1x = second.longitude.doubleValue()
				- first.longitude.doubleValue();
		Double v1y = second.latitude.doubleValue()
				- first.latitude.doubleValue();
		Double v2x = forth.longitude.doubleValue()
				- third.longitude.doubleValue();
		Double v2y = forth.longitude.doubleValue()
				- third.longitude.doubleValue();

		return (v1x * v2x + v1y * v2y)
				/ (Math.sqrt(v1x * v1x + v1y * v1y) * Math.sqrt(v2x * v2x + v2y
						* v2y));
	}

	/**
	 * 
	 * @param center
	 * @param point
	 * @return angle in radius
	 */
	public static Double angle(GeoPoint center, GeoPoint point) {
		Double x = point.longitude.subtract(center.longitude).doubleValue();
		Double y = point.latitude.subtract(center.latitude).doubleValue();
		Double radius = Math.acos(x / Math.sqrt(x * x + y * y));
		if (y < 0)
			radius = 2 * Math.PI - radius;
		return radius;
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
	 *            distance in meter
	 * @param lat
	 *            latitude nearby
	 * @return delta of latitude
	 */
	public static BigDecimal distanceToLat(BigDecimal distance, BigDecimal lat) {
		BigDecimal factor = new BigDecimal(111132.954 - 559.822
				* Math.cos(Math.toRadians(2 * lat.doubleValue())) + 1.175
				* Math.cos(Math.toRadians(4 * lat.doubleValue())));
		return distance.divide(factor, PRECISION, BigDecimal.ROUND_HALF_UP);
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
	 * @param lat
	 * @return
	 */
	public static BigDecimal distanceToLong(BigDecimal distance, BigDecimal lat) {
		BigDecimal factor = new BigDecimal(Math.PI * 6378137
				* Math.cos(Math.toRadians(lat.doubleValue())) / 180).abs();
		return distance.divide(factor, PRECISION, BigDecimal.ROUND_HALF_UP);
	}

	public static BigDecimal longToDist(BigDecimal lat, BigDecimal delta) {
		BigDecimal factor = new BigDecimal(Math.PI * 6378137
				* Math.cos(Math.toRadians(lat.doubleValue())) / 180);
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

	/**
	 * Convert Distance to degree differences
	 * 
	 * @param near
	 * @param distance
	 * @return
	 */
	public static BigDecimal geodiff(GeoPoint near, BigDecimal distance) {
		BigDecimal latdelta = GeoUtils.distanceToLat(distance, near.latitude);
		BigDecimal longdelta = GeoUtils.distanceToLong(distance, near.latitude);
		return latdelta.add(longdelta).divide(TWO, PRECISION,
				BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * Get the overlap of two ranges
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public static GeoRange overlap(GeoRange a, GeoRange b) {
		GeoPoint newpoint = new GeoPoint(
				max(a.start.latitude, b.start.latitude), max(a.start.longitude,
						b.start.longitude));
		GeoDimension newdim = new GeoDimension(min(
				a.start.latitude.add(a.size.latRange).subtract(
						newpoint.latitude),
				b.start.latitude.add(b.size.latRange).subtract(
						newpoint.latitude)), min(
				a.start.longitude.add(a.size.longRange).subtract(
						newpoint.longitude),
				b.start.longitude.add(b.size.longRange).subtract(
						newpoint.longitude)));
		if (newdim.latRange.compareTo(BigDecimal.ZERO) < 0
				|| newdim.longRange.compareTo(BigDecimal.ZERO) < 0)
			return null;
		return new GeoRange(newpoint, newdim);
	}

	public static BigDecimal max(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) > 0 ? a : b;
	}

	public static BigDecimal min(BigDecimal a, BigDecimal b) {
		return a.compareTo(b) < 0 ? a : b;
	}

}
