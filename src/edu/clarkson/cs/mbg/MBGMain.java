package edu.clarkson.cs.mbg;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.mbg.geo.GeoDimension;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoRange;
import edu.clarkson.cs.mbg.topology.GraphService;
import edu.clarkson.cs.mbg.topology.model.Graph;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;

public class MBGMain {

	// This is the US territory range
	static final GeoRange territory = new GeoRange(new GeoPoint(24, -125),
			new GeoDimension(26, 59));

	public static void main(String[] args) {
		System.loadLibrary("jcsdp");
		new MBGContextSet().apply();

		TraceDataService tdService = BeanContext.get().get("traceDataService");
		GraphService graphService = BeanContext.get().get("graphService");
		// TODO Use RipeAtlas to create measurement
		// int measurementId = 1717329;
		int measurementId = 1717512;

		Graph traceGraph = tdService.getTraceGraph(measurementId);
		System.out.println(traceGraph.getEntrance().size());
		// traceGraph
		// .merge(new Node("72.230.153.211"), new Node("72.230.153.213"));
		graphService.heavyduty(traceGraph);

		return;
	}
}
