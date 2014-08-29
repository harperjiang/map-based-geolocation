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
@Table(name = "ping_probe")
public class PingProbe implements EntityObject {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;

	@Column(name = "ip_address")
	String ipAddress;

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
