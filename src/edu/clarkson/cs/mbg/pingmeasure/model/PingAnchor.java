package edu.clarkson.cs.mbg.pingmeasure.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import edu.clarkson.cs.persistence.EntityObject;

@Entity
@Table(name = "ping_anchor")
public class PingAnchor implements EntityObject {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(name = "ip_address")
	String ipAddress;

	@Column(name = "anchor_6010")
	BigDecimal anchor6010;

	@Column(name = "anchor_6024")
	BigDecimal anchor6024;

	@Column(name = "anchor_6045")
	BigDecimal anchor6045;

	@Column(name = "anchor_6061")
	BigDecimal anchor6061;

	@Column(name = "anchor_6062")
	BigDecimal anchor6062;

	@Column(name = "anchor_6065")
	BigDecimal anchor6065;

	@Column(name = "anchor_6072")
	BigDecimal anchor6072;

	@Column(name = "latitude")
	BigDecimal latitude;

	@Column(name = "longitude")
	BigDecimal longitude;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public BigDecimal getAnchor6010() {
		return anchor6010;
	}

	public void setAnchor6010(BigDecimal anchor6010) {
		this.anchor6010 = anchor6010;
	}

	public BigDecimal getAnchor6024() {
		return anchor6024;
	}

	public void setAnchor6024(BigDecimal anchor6024) {
		this.anchor6024 = anchor6024;
	}

	public BigDecimal getAnchor6045() {
		return anchor6045;
	}

	public void setAnchor6045(BigDecimal anchor6045) {
		this.anchor6045 = anchor6045;
	}

	public BigDecimal getAnchor6061() {
		return anchor6061;
	}

	public void setAnchor6061(BigDecimal anchor6061) {
		this.anchor6061 = anchor6061;
	}

	public BigDecimal getAnchor6062() {
		return anchor6062;
	}

	public void setAnchor6062(BigDecimal anchor6062) {
		this.anchor6062 = anchor6062;
	}

	public BigDecimal getAnchor6065() {
		return anchor6065;
	}

	public void setAnchor6065(BigDecimal anchor6065) {
		this.anchor6065 = anchor6065;
	}

	public BigDecimal getAnchor6072() {
		return anchor6072;
	}

	public void setAnchor6072(BigDecimal anchor6072) {
		this.anchor6072 = anchor6072;
	}

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
