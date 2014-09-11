package edu.clarkson.cs.mbg.geo;

import static org.junit.Assert.assertEquals;
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

		assertTrue(Math.abs(GeoUtils.distanceToLong(new BigDecimal("107550"),
				new BigDecimal(15)).doubleValue() - 1) < 0.001);
		assertTrue(Math.abs(GeoUtils.distanceToLong(new BigDecimal("96486"),
				new BigDecimal(30)).doubleValue() - 1) < 0.001);
		assertTrue(Math.abs(GeoUtils.distanceToLong(new BigDecimal("78847"),
				new BigDecimal(45)).doubleValue() - 1) < 0.01);
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
	
	@Test
	public void testLatToDist() {
//		assertEquals(0, GeoUtils.longToDist(new BigDecimal("44.951488"), new BigDecimal("1.2819")));
		assertEquals(0, GeoUtils.longToDist(new BigDecimal("28.1765"), new BigDecimal("1.2819")));
	}
}
