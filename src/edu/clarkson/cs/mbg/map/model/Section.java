package edu.clarkson.cs.mbg.map.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import edu.clarkson.cs.persistence.EntityObject;

@Entity
@Table(name = "road_section")
public class Section implements EntityObject {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "name_2")
	private String name2;

	@Column(name = "prefix_dir")
	private String prefixDir;

	@Column(name = "prefix_type")
	private String prefixType;

	@Column(name = "prefix_num")
	private String prefixNum;

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

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "road_waypoint_simple", joinColumns = @JoinColumn(name = "road"))
	@OrderBy("sequence ASC")
	private List<Waypoint> waypoints;

	@Column(name = "lat_max")
	private BigDecimal latMax;

	@Column(name = "lat_min")
	private BigDecimal latMin;

	@Column(name = "long_max")
	private BigDecimal longMax;

	@Column(name = "long_min")
	private BigDecimal longMin;

	public Section() {
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

	public String getPrefixDir() {
		return prefixDir;
	}

	public void setPrefixDir(String prefixDir) {
		this.prefixDir = prefixDir;
	}

	public String getPrefixType() {
		return prefixType;
	}

	public void setPrefixType(String prefixType) {
		this.prefixType = prefixType;
	}

	public String getPrefixNum() {
		return prefixNum;
	}

	public void setPrefixNum(String prefixNum) {
		this.prefixNum = prefixNum;
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
		waypoint.setSequence(getWaypoints().size());
		getWaypoints().add(waypoint);
	}

	public BigDecimal getLatMax() {
		return latMax;
	}

	public void setLatMax(BigDecimal latMax) {
		this.latMax = latMax;
	}

	public BigDecimal getLatMin() {
		return latMin;
	}

	public void setLatMin(BigDecimal latMin) {
		this.latMin = latMin;
	}

	public BigDecimal getLongMax() {
		return longMax;
	}

	public void setLongMax(BigDecimal longMax) {
		this.longMax = longMax;
	}

	public BigDecimal getLongMin() {
		return longMin;
	}

	public void setLongMin(BigDecimal longMin) {
		this.longMin = longMin;
	}

}
