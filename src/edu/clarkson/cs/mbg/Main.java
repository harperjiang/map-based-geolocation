package edu.clarkson.cs.mbg;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.map.DistanceUtils;
import edu.clarkson.cs.mbg.map.dao.RoadDao;
import edu.clarkson.cs.mbg.map.search.MapSearcher;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;
import edu.clarkson.cs.mbg.tracedata.model.TraceData;

public class Main {

	public static void main(String[] args) {
		new MBGContextSet().apply();

		TraceDataService tdService = BeanContext.get().get("traceDataService");
		RoadDao roadDao = BeanContext.get().get("roadDao");

		// TODO Use RipeAtlas to create measurement
		int measurementId = 1708286;

		Map<Probe, List<TraceData>> result = tdService
				.loadMeasurementResult(measurementId);

		MapSearcher mapSearcher = new MapSearcher();
		mapSearcher.setRoadDao(roadDao);

		for (Entry<Probe, List<TraceData>> entry : result.entrySet()) {
			// TODO A single point lead to too many possibilities. So I choose
			// to only look at traceroutes that provide enough information
			if (entry.getValue().size() > 3) {
				GeoPoint startPoint = new GeoPoint(
						entry.getKey().getLatitude(), entry.getKey()
								.getLongitude());

				mapSearcher.addKnown(entry.getKey().getAddressV4(), startPoint);

				for (TraceData data : entry.getValue()) {
					mapSearcher.addUnknown(data.getToIp(), data.getFromIp(),
							DistanceUtils.timeToDistance(data.getRtt()));
				}
			}
		}

		return;
	}
}
