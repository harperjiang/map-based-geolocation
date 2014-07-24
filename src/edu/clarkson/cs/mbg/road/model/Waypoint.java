package edu.clarkson.cs.mbg.road.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "road_waypoint")
public class Waypoint {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "road", referencedColumnName = "id", nullable = false, updatable = false)
	private Road road;

	@Column(name = "point_x")
	private BigDecimal pointX;

	@Column(name = "point_y")
	private BigDecimal pointY;

	public Road getRoad() {
		return road;
	}

	public void setRoad(Road road) {
		this.road = road;
	}

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
