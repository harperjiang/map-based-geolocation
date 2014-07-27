package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;

public class GeoDimension {

	public BigDecimal latRange;

	public BigDecimal longRange;

	public GeoDimension(BigDecimal lat, BigDecimal longi) {
		this.latRange = lat;
		this.longRange = longi;
	}
}
