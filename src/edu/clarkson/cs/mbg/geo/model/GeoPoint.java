package edu.clarkson.cs.mbg.geo.model;

import java.math.BigDecimal;

public class GeoPoint {

	BigDecimal latitude;

	BigDecimal longitude;

	public BigDecimal getLatitude() {
		return latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

}
