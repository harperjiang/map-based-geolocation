package edu.clarkson.cs.mbg.geo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Arrays;

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

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof GeoPoint))
			return super.equals(obj);
		GeoPoint ap = (GeoPoint) obj;
		return ap.latitude.equals(this.latitude)
				&& ap.longitude.equals(this.longitude);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(new Object[] { latitude, longitude });
	}
}
