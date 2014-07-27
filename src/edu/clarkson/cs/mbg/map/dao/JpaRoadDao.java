package edu.clarkson.cs.mbg.map.dao;

import java.util.List;

import edu.clarkson.cs.mbg.geo.GeoRange;
import edu.clarkson.cs.mbg.map.model.RoadType;
import edu.clarkson.cs.mbg.map.model.Section;
import edu.clarkson.cs.persistence.JpaEntityDao;

public class JpaRoadDao extends JpaEntityDao<Section> implements RoadDao {

	@Override
	public List<Section> getSectionInRange(GeoRange range) {
		String sql = "select r from Section r "
				+ "where r.prefixType in (:a,:b,:c) "
				+ "and (r.latMax > :range_lat_min and r.latMin < :range_lat_max) "
				+ "and (r.longMax > :range_long_min and r.longMin < :range_long_max)";
		return getEntityManager()
				.createQuery(sql, Section.class)
				.setParameter("a", RoadType.INTERSTATE)
				.setParameter("b", RoadType.STATE_ROUTE)
				.setParameter("c", RoadType.US_ROUTE)
				.setParameter("range_lat_min", range.start.latitude)
				.setParameter("range_lat_max",
						range.start.latitude.add(range.size.latRange))
				.setParameter("range_long_min", range.start.longitude)
				.setParameter("range_long_max",
						range.start.longitude.add(range.size.longRange))
				.getResultList();
	}

}
