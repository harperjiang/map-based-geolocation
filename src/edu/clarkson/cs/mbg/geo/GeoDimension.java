package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class GeoDimension {

	public BigDecimal latRange;

	public BigDecimal longRange;

	public GeoDimension(Number lat, Number longi) {
		this.latRange = new BigDecimal(String.valueOf(lat));
		this.longRange = new BigDecimal(String.valueOf(longi));
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0},{1}", latRange, longRange);
	}
}
