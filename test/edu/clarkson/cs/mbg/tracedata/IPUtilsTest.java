package edu.clarkson.cs.mbg.tracedata;

import static org.junit.Assert.*;

import org.junit.Test;

public class IPUtilsTest extends IPUtils {

	@Test
	public void testSamePrefix() {
		assertTrue(IPUtils.samePrefix("192.165.29.32", "192.165.12.44", 16));
		assertFalse(IPUtils.samePrefix("192.165.29.32", "192.165.12.44", 24));
		assertTrue(IPUtils.samePrefix("192.165.12.32", "192.165.12.44", 24));
		assertFalse(IPUtils.samePrefix("192.165.12.32", "192.165.12.44", 30));
	}

}
