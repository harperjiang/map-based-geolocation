package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class GeoDimension {

	public BigDecimal latRange;

	public BigDecimal longRange;

	public GeoDimension(BigDecimal lat, BigDecimal longi) {
		this.latRange = lat;
		this.longRange = longi;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0},{1}", latRange, longRange);
	}
}
