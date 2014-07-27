package edu.clarkson.cs.mbg;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.map.DistanceUtils;
import edu.clarkson.cs.mbg.map.search.MapSearcher;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;
import edu.clarkson.cs.mbg.tracedata.model.TraceData;

public class Main {

	public static void main(String[] args) {
		new MBGContextSet().apply();
		// TODO Use RipeAtlas to create measurement
		int measurementId = 1708286;

		TraceDataService tdService = BeanContext.get().get("traceDataService");

		Map<Probe, List<TraceData>> result = tdService
				.loadMeasurementResult(measurementId);

		MapSearcher mapSearcher = new MapSearcher();

		for (Entry<Probe, List<TraceData>> entry : result.entrySet()) {
			GeoPoint startPoint = new GeoPoint(entry.getKey().getLatitude(),
					entry.getKey().getLongitude());

			mapSearcher.addKnown(entry.getKey().getAddressV4(), startPoint);

			for (TraceData data : entry.getValue()) {
				mapSearcher.addUnknown(data.getToIp(), data.getFromIp(),
						DistanceUtils.timeToDistance(data.getRtt()));
			}
		}
	}
}
