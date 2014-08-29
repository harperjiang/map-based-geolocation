package edu.clarkson.cs.mbg.map.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import edu.clarkson.cs.persistence.EntityObject;

@Entity
@Table(name = "city_website")
public class CityWebsite implements EntityObject {

	@Id
	@Column(name = "city", updatable = false, insertable = false)
	private Integer id;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city", referencedColumnName = "id")
	private City city;

	@Column(name = "website")
	private String website;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "remark")
	private String remark;

	@ElementCollection(fetch = FetchType.LAZY)
	@CollectionTable(name = "city_website_ips", joinColumns = { @JoinColumn(referencedColumnName = "city", name = "city") })
	@Column(name = "ip_address")
	private Set<String> ipAddresses;

	public CityWebsite() {
		super();
		ipAddresses = new HashSet<String>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		throw new UnsupportedOperationException();
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
		this.id = city.getId();
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<String> getIpAddresses() {
		return ipAddresses;
	}

}
