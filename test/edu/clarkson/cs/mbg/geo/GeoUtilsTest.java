package edu.clarkson.cs.mbg.geo;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

public class GeoUtilsTest {

	@Test
	public void testDistanceToLat() {
		assertTrue(Math.abs(GeoUtils.distanceToLat(new BigDecimal("111694"),
				new BigDecimal(90)).doubleValue() - 1) < 0.001);
	}

	@Test
	public void testDistanceToLong() {
		assertTrue(Math.abs(GeoUtils.distanceToLong(new BigDecimal("111320"),
				new BigDecimal(0)).doubleValue() - 1) < 0.001);
	}

	@Test
	public void testBuildFromCenter() {
		GeoRange range = GeoUtils.buildFromCenter(new GeoPoint(new BigDecimal(
				"36.933"), new BigDecimal("-121.309")), new BigDecimal(33800));
		assertTrue(range.size.latRange.compareTo(BigDecimal.ZERO) > 0);
		assertTrue(range.size.longRange.compareTo(BigDecimal.ZERO) > 0);
	}

	@Test
	public void testAngle() {
		Double angle = GeoUtils
				.angle(new GeoPoint(0, 0), new GeoPoint(-0.1, 1));
		assertTrue(angle > 3 * Math.PI / 2 && angle < 2 * Math.PI);
	}
}
