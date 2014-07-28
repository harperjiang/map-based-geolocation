package edu.clarkson.cs.mbg.map.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoUtils;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
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

		Map<Section, List<GeoPoint>> roadWithPoints = new HashMap<Section, List<GeoPoint>>();
		for (Section section : sections) {
			// Look for points on the road that satisfy the situation
			roadWithPoints.put(section,
					pointsOnRoad(from.getLocation(), section, distance));
		}

		List<GeoPoint> consolidated = consolidate(from.getLocation(),
				roadWithPoints);

		for (GeoPoint point : consolidated) {
			if (angleCheck(from, point)) {
				// Add this point to the candidate list
				getIndex(label).add(new SearchNode(from, label, point));
			}
		}
	}

	static int BUCKET_COUNT = 36;

	/**
	 * Divide circle into buckets, leave only one point in each bucket
	 * 
	 * @param center
	 * @param rwp
	 * @return
	 */
	protected List<GeoPoint> consolidate(GeoPoint center,
			Map<Section, List<GeoPoint>> rwp) {
		List<List<GeoPoint>> buckets = new ArrayList<List<GeoPoint>>();
		for (int i = 0; i < BUCKET_COUNT; i++)
			buckets.add(new ArrayList<GeoPoint>());

		// Put points into buckets
		for (Entry<Section, List<GeoPoint>> entry : rwp.entrySet()) {
			for (GeoPoint point : entry.getValue()) {
				Double degree = Math.toDegrees(GeoUtils.angle(center, point));
				int index = (int) (degree / (360 / BUCKET_COUNT));
				// TODO Should I focus more on big route?
				buckets.get(index).add(point);
			}
		}

		List<GeoPoint> results = new ArrayList<GeoPoint>();
		// One from each bucket
		for (List<GeoPoint> bucket : buckets) {
			if (bucket.size() > 0)
				results.add(bucket.get(0));
		}
		return results;
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
		Double cosval = GeoUtils.angleInCos(from.getBase().getLocation(),
				from.getLocation(), from.getLocation(), point);
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

		// TODO This is a threshold value setting.
		// The road that is too close to the center will be ignored
		if (maxValue.compareTo(limit.multiply(new BigDecimal(0.9))) < 0) {
			return results;
		}

		// Search forward
		Waypoint forwardSat = section.getWaypoints().get(closeIndex);
		BigDecimal forwardDiff = limit.subtract(closeValue);
		for (int i = closeIndex; i < section.getWaypoints().size(); i++) {
			Waypoint wp = section.getWaypoints().get(i);
			BigDecimal dist = GeoUtils.distance(location, wp.asGeoPoint());
			BigDecimal thisdiff = dist.subtract(limit).abs();
			if (dist.compareTo(limit) < 0
					&& thisdiff.compareTo(forwardDiff) < 0) {
				// In range and is the closest
				forwardSat = wp;
				forwardDiff = thisdiff;
			}
		}

		// Search backward
		Waypoint backwardSat = section.getWaypoints().get(closeIndex);
		BigDecimal backwardDiff = limit.subtract(closeValue);
		for (int i = closeIndex; i >= 0; i--) {
			Waypoint wp = section.getWaypoints().get(i);
			BigDecimal dist = GeoUtils.distance(location, wp.asGeoPoint());
			BigDecimal thisdiff = dist.subtract(limit).abs();
			if (dist.compareTo(limit) < 0
					&& thisdiff.compareTo(backwardDiff) < 0) {
				// In range and is the closest
				backwardSat = wp;
				backwardDiff = thisdiff;
			}
		}

		// Check candidate directions.
		// If they are in roughly the same direction (cos(theta) > 0), leave
		// only one
		if (GeoUtils.angleInCos(location, forwardSat.asGeoPoint(), location,
				backwardSat.asGeoPoint()) > 0) {
			int compare = forwardDiff.compareTo(backwardDiff);
			if (compare > 0)
				results.add(backwardSat.asGeoPoint());
			else
				results.add(forwardSat.asGeoPoint());

		} else {
			results.add(forwardSat.asGeoPoint());
			results.add(backwardSat.asGeoPoint());
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
