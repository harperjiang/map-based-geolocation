package edu.clarkson.cs.mbg.map.search;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoUtils;
import edu.clarkson.cs.mbg.map.dao.CityDao;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
import edu.clarkson.cs.mbg.map.model.Section;

public class MapSearcher {

	private List<SearchNode> root;

	private Map<String, List<SearchNode>> index;

	private CityDao cityDao;

	private RoadDao roadDao;

	public MapSearcher() {
		super();
		root = new ArrayList<SearchNode>();
		index = new HashMap<String, List<SearchNode>>();
	}

	public void addKnown(String address, GeoPoint startPoint) {
		root.add(new SearchNode(address, startPoint));
	}

	public void addUnknown(String label, String from, BigDecimal distance) {
		for (SearchNode start : getIndex(from)) {
			search(start, distance, label);
		}
	}

	protected SearchNode search(SearchNode from, BigDecimal distance,
			String label) {
		List<Section> sections = roadDao.getSectionInRange(GeoUtils
				.buildFromCenter(from.getLocation(), distance));
		
		
		return null;
	}

	protected List<SearchNode> getIndex(String label) {
		if (!index.containsKey(label)) {
			index.put(label, new ArrayList<SearchNode>());
		}
		return index.get(label);
	}
}
