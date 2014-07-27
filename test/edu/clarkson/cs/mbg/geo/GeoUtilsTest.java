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

}
