package edu.clarkson.cs.mbg;

import java.util.List;
import java.util.Map;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput.TracerouteData;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;

public class Main {

	public static void main(String[] args) {
		new MBGContextSet().apply();

		TraceDataService tdService = BeanContext.get().get("traceDataService");
		
		// TODO Use RipeAtlas to create measurement
		int measurementId = 1708286;

		Map<Probe, List<TracerouteData>> result = tdService
				.loadMeasurementResult(measurementId);

		

		return;
	}
}
