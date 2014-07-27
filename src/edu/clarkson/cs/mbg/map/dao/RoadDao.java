package edu.clarkson.cs.mbg.map.dao;

import java.util.List;

import edu.clarkson.cs.mbg.geo.GeoRange;
import edu.clarkson.cs.mbg.map.model.Section;

public interface RoadDao {
	
	public Section find(int roadId);

	public void save(Section road);

	public List<Section> getSectionInRange(GeoRange range);
}
