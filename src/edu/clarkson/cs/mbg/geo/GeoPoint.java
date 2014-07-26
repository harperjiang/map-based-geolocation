package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;

public class GeoPoint {

	public BigDecimal latitude;

	public BigDecimal longitude;

	public GeoPoint(Number lat, Number longi) {
		this.latitude = new BigDecimal(String.valueOf(lat));
		this.longitude = new BigDecimal(String.valueOf(longi));
	}

}
