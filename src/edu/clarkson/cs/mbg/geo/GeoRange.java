package edu.clarkson.cs.mbg.geo;

import java.text.MessageFormat;

public class GeoRange {

	public GeoPoint start;

	public GeoDimension size;

	public GeoRange(GeoPoint start, GeoDimension size) {
		super();
		this.start = start;
		this.size = size;
	}

	@Override
	public String toString() {
		return MessageFormat.format("{0},{1}", start, size);
	}
}
