package edu.clarkson.cs.mbg.map.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Waypoint {

	@Column(name = "point_x")
	private BigDecimal pointX;

	@Column(name = "point_y")
	private BigDecimal pointY;

	public BigDecimal getPointX() {
		return pointX;
	}

	public void setPointX(BigDecimal pointX) {
		this.pointX = pointX;
	}

	public BigDecimal getPointY() {
		return pointY;
	}

	public void setPointY(BigDecimal pointY) {
		this.pointY = pointY;
	}

}
