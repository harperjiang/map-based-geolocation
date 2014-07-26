package edu.clarkson.cs.mbg.map.search;

import edu.clarkson.cs.mbg.geo.GeoPoint;

public class SearchNode {

	private SearchNode base;

	private String label;

	private GeoPoint location;

	public SearchNode(String label, GeoPoint point) {
		this.label = label;
		this.location = point;
	}

	public SearchNode(SearchNode base, String label, GeoPoint point) {
		super();
		this.base = base;
		this.label = label;
		this.location = point;
	}

	public SearchNode getBase() {
		return base;
	}

	public void setBase(SearchNode base) {
		this.base = base;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public GeoPoint getLocation() {
		return location;
	}

	public void setLocation(GeoPoint location) {
		this.location = location;
	}

}
