package edu.clarkson.cs.mbg.map.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoUtils;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
import edu.clarkson.cs.mbg.map.model.RoadType;
import edu.clarkson.cs.mbg.map.model.Section;
import edu.clarkson.cs.mbg.map.model.Waypoint;

public class MapSearcher {

	private List<SearchNode> root;

	private Map<String, List<SearchNode>> index;

	private RoadDao roadDao;

	public MapSearcher() {
		super();
		root = new ArrayList<SearchNode>();
		index = new HashMap<String, List<SearchNode>>();
	}

	public void addKnown(String address, GeoPoint startPoint) {
		SearchNode node = new SearchNode(address, startPoint);
		root.add(node);
		getIndex(address).add(node);
	}

	public void addUnknown(String label, String from, BigDecimal distance) {
		for (SearchNode start : getIndex(from)) {
			search(start, distance, label);
		}
	}

	protected void search(SearchNode from, BigDecimal distance, String label) {
		List<Section> sections = roadDao.getSectionInRange(GeoUtils
				.buildFromCenter(from.getLocation(), distance));

		for (Section section : sections) {
			// Look for points on the road that satisfy the situation
			List<GeoPoint> points = pointsOnRoad(from.getLocation(), section,
					distance);
			for (GeoPoint point : points) {
				if (angleCheck(from, point)) {
					// Add this point to the candidate list
					getIndex(label).add(new SearchNode(from, label, point));
				}
			}
		}
	}

	/**
	 * This method check the candidate points angle and makes sure that search
	 * path keeps going forward.
	 * 
	 * @param from
	 * @param point
	 * @return
	 */
	private boolean angleCheck(SearchNode from, GeoPoint point) {
		if (from.getBase() == null)
			return true;
		Double cosval = GeoUtils.angle(from.getBase().getLocation(),
				from.getLocation(), point);
		return cosval >= 0;
	}

	/**
	 * Looks for
	 * 
	 * @param location
	 * @param section
	 * @param distance
	 * @return
	 */
	private List<GeoPoint> pointsOnRoad(GeoPoint location, Section section,
			BigDecimal limit) {
		List<GeoPoint> results = new ArrayList<GeoPoint>();
		// Only go for big road (interstate/state route/us route)
		// TODO Is this assumption correct?
		if (!(RoadType.INTERSTATE.equals(section.getPrefixType())
				|| RoadType.STATE_ROUTE.equals(section.getPrefixType()) || RoadType.US_ROUTE
					.equals(section.getPrefixType()))) {
			return results;
		}

		int closeIndex = -1;
		BigDecimal maxValue = BigDecimal.ZERO;
		BigDecimal closeValue = limit.multiply(BigDecimal.TEN);
		for (int i = 0; i < section.getWaypoints().size(); i++) {
			BigDecimal dist = GeoUtils.distance(location, section
					.getWaypoints().get(i).asGeoPoint());
			if (dist.compareTo(closeValue) < 0) {
				closeIndex = i;
				closeValue = dist;
			}
			if (dist.compareTo(maxValue) > 0) {
				maxValue = dist;
			}
		}
		if (closeValue.compareTo(limit) > 0) {
			// The closest point is out of range
			return results;
		}
		if (maxValue.compareTo(limit) < 0) {
			return results;
		}

		// Search forward
		Waypoint sat = null;
		BigDecimal diff = limit;
		for (int i = closeIndex; i < section.getWaypoints().size(); i++) {
			Waypoint wp = section.getWaypoints().get(i);
			BigDecimal dist = GeoUtils.distance(location, wp.asGeoPoint());
			BigDecimal thisdiff = dist.subtract(limit).abs();
			if (dist.compareTo(limit) < 0 && thisdiff.compareTo(diff) < 0) {
				// In range and is the closest
				sat = wp;
				diff = thisdiff;
			}
		}
		if (sat != null) {
			results.add(sat.asGeoPoint());
		}

		// Search backward
		sat = null;
		diff = limit;
		for (int i = closeIndex; i > 0; i--) {
			Waypoint wp = section.getWaypoints().get(i);
			BigDecimal dist = GeoUtils.distance(location, wp.asGeoPoint());
			BigDecimal thisdiff = dist.subtract(limit).abs();
			if (dist.compareTo(limit) < 0 && thisdiff.compareTo(diff) < 0) {
				// In range and is the closest
				sat = wp;
				diff = thisdiff;
			}
		}
		if (sat != null) {
			results.add(sat.asGeoPoint());
		}

		return results;
	}

	protected List<SearchNode> getIndex(String label) {
		if (!index.containsKey(label)) {
			index.put(label, new ArrayList<SearchNode>());
		}
		return index.get(label);
	}

	public RoadDao getRoadDao() {
		return roadDao;
	}

	public void setRoadDao(RoadDao roadDao) {
		this.roadDao = roadDao;
	}

}
