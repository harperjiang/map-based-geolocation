package edu.clarkson.cs.mbg.map.dao;

import java.util.List;

import edu.clarkson.cs.mbg.geo.GeoRange;
import edu.clarkson.cs.mbg.map.model.Section;
import edu.clarkson.cs.persistence.JpaEntityDao;

public class JpaRoadDao extends JpaEntityDao<Section> implements RoadDao {

	@Override
	public List<Section> getSectionInRange(GeoRange range) {
		throw new RuntimeException("Not implemented");
	}

}
