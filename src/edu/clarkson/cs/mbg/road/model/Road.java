package edu.clarkson.cs.mbg.road.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "road")
public class Road {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "name_2")
	private String name2;

	@Column(name = "route_dir")
	private String routeDir;

	@Column(name = "route_type")
	private String routeType;

	@Column(name = "route_num")
	private String routeNum;

	@Column(name = "street_type")
	private String streetType;

	@Column(name = "suffix_dir")
	private String suffixDir;

	@Column(name = "qualifier")
	private String qualifier;

	@Column(name = "trans_type")
	private String transType;

	@Column(name = "fcode")
	private String fcode;

	@Column(name = "state")
	private String state;

	@Column(name = "miles")
	private BigDecimal miles;

	@Column(name = "kilometers")
	private BigDecimal kilometers;

	@Column(name = "shape_length")
	private BigDecimal shapeLength;

	@OneToMany(mappedBy = "road", cascade = { CascadeType.ALL })
	private List<Waypoint> waypoints;

	public Road() {
		super();
		waypoints = new ArrayList<Waypoint>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName2() {
		return name2;
	}

	public void setName2(String name2) {
		this.name2 = name2;
	}

	public String getRouteDir() {
		return routeDir;
	}

	public void setRouteDir(String routeDir) {
		this.routeDir = routeDir;
	}

	public String getRouteType() {
		return routeType;
	}

	public void setRouteType(String routeType) {
		this.routeType = routeType;
	}

	public String getRouteNum() {
		return routeNum;
	}

	public void setRouteNum(String routeNum) {
		this.routeNum = routeNum;
	}

	public String getStreetType() {
		return streetType;
	}

	public void setStreetType(String streetType) {
		this.streetType = streetType;
	}

	public String getSuffixDir() {
		return suffixDir;
	}

	public void setSuffixDir(String suffixDir) {
		this.suffixDir = suffixDir;
	}

	public String getQualifier() {
		return qualifier;
	}

	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getFcode() {
		return fcode;
	}

	public void setFcode(String fcode) {
		this.fcode = fcode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public BigDecimal getMiles() {
		return miles;
	}

	public void setMiles(BigDecimal miles) {
		this.miles = miles;
	}

	public BigDecimal getKilometers() {
		return kilometers;
	}

	public void setKilometers(BigDecimal kilometers) {
		this.kilometers = kilometers;
	}

	public BigDecimal getShapeLength() {
		return shapeLength;
	}

	public void setShapeLength(BigDecimal shapeLength) {
		this.shapeLength = shapeLength;
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void addWaypoint(Waypoint waypoint) {
		waypoint.setRoad(this);
		getWaypoints().add(waypoint);
	}

}
