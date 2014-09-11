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
@Table(name = "landmark")
public class Landmark implements EntityObject {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(name = "latitude")
	BigDecimal latitude;

	@Column(name = "longitude")
	BigDecimal longitude;

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

	// Minneapolis, MN, 9233
	@Column(name = "probe_01")
	BigDecimal probe01;

	// Denver, CO, 17417
	@Column(name = "probe_02")
	BigDecimal probe02;

	// Bozeman, MT, 18395
	@Column(name = "probe_03")
	BigDecimal probe03;

	// Atlanta, GA, 14589
	@Column(name = "probe_04")
	BigDecimal probe04;

	// Detroit, MI, 3562
	@Column(name = "probe_05")
	BigDecimal probe05;

	// Chicago, IL, 987
	@Column(name = "probe_06")
	BigDecimal probe06;

	// St. Louis, MO, 4987
	@Column(name = "probe_07")
	BigDecimal probe07;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public BigDecimal getProbe01() {
		return probe01;
	}

	public void setProbe01(BigDecimal probe01) {
		this.probe01 = probe01;
	}

	public BigDecimal getProbe02() {
		return probe02;
	}

	public void setProbe02(BigDecimal probe02) {
		this.probe02 = probe02;
	}

	public BigDecimal getProbe03() {
		return probe03;
	}

	public void setProbe03(BigDecimal probe03) {
		this.probe03 = probe03;
	}

	public BigDecimal getProbe04() {
		return probe04;
	}

	public void setProbe04(BigDecimal probe04) {
		this.probe04 = probe04;
	}

	public BigDecimal getProbe05() {
		return probe05;
	}

	public void setProbe05(BigDecimal probe05) {
		this.probe05 = probe05;
	}

	public BigDecimal getProbe06() {
		return probe06;
	}

	public void setProbe06(BigDecimal probe06) {
		this.probe06 = probe06;
	}

	public BigDecimal getProbe07() {
		return probe07;
	}

	public void setProbe07(BigDecimal probe07) {
		this.probe07 = probe07;
	}

}
