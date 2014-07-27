package edu.clarkson.cs.mbg.map.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoUtils;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
import edu.clarkson.cs.mbg.map.model.Section;

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
			GeoPoint[] points = pointsOnRoad(from.getLocation(), section,
					distance);

			for (GeoPoint point : points) {
				if (angleCheck(from, point)) {
					// Add this point to the candidate list
					getIndex(label).add(new SearchNode(from, label, point));
				}
			}
		}
	}

	// This method makes sure that search path keeps going forward
	private boolean angleCheck(SearchNode from, GeoPoint point) {
		throw new RuntimeException("Not implemented");
	}

	private GeoPoint[] pointsOnRoad(GeoPoint location, Section section,
			BigDecimal distance) {
		throw new RuntimeException("Not implemented");
	}

	protected List<SearchNode> getIndex(String label) {
		if (!index.containsKey(label)) {
			index.put(label, new ArrayList<SearchNode>());
		}
		return index.get(label);
	}
}
