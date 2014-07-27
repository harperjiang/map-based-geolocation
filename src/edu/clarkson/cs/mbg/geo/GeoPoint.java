package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;
import java.text.MessageFormat;

public class GeoPoint {

	public BigDecimal latitude;

	public BigDecimal longitude;

	public GeoPoint(Number lat, Number longi) {
		this.latitude = new BigDecimal(String.valueOf(lat));
		this.longitude = new BigDecimal(String.valueOf(longi));
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0},{1}", latitude, longitude);
	}
}
