package edu.clarkson.cs.mbg.opt;

import java.math.BigDecimal;

import edu.clarkson.cs.mbg.geo.GeoPoint;

public class DistConstraint {

	private GeoPoint from;

	private BigDecimal objective;

	private BigDecimal upperbound;

	private BigDecimal lowerbound;

	public DistConstraint(GeoPoint from, BigDecimal obj, BigDecimal upper, BigDecimal lower) {
		this.from = from;
		this.objective = obj;
		this.upperbound = upper;
		this.lowerbound = lower;
	}

	public GeoPoint getFrom() {
		return from;
	}

	public BigDecimal getObjective() {
		return objective;
	}

	public BigDecimal getUpperbound() {
		return upperbound;
	}

	public BigDecimal getLowerbound() {
		return lowerbound;
	}

}
