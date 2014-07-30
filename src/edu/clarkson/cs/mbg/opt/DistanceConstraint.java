package edu.clarkson.cs.mbg.opt;

import java.math.BigDecimal;

import edu.clarkson.cs.mbg.geo.GeoPoint;

public class DistanceConstraint {

	private GeoPoint from;

	private BigDecimal distance;

	public DistanceConstraint(GeoPoint from, BigDecimal distance) {
		this.from = from;
		this.distance = distance;
	}

	public GeoPoint getFrom() {
		return from;
	}

	public void setFrom(GeoPoint from) {
		this.from = from;
	}

	public BigDecimal getDistance() {
		return distance;
	}

	public void setDistance(BigDecimal distance) {
		this.distance = distance;
	}

}
