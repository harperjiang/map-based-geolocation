package edu.clarkson.cs.mbg;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput.TracerouteData;
import edu.clarkson.cs.mbg.geo.GeoDimension;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoRange;
import edu.clarkson.cs.mbg.geo.GeoUtils;
import edu.clarkson.cs.mbg.map.DistanceUtils;
import edu.clarkson.cs.mbg.opt.DistConstraint;
import edu.clarkson.cs.mbg.opt.SinglePointEstimator;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary("jcsdp");
		new MBGContextSet().apply();

		TraceDataService tdService = BeanContext.get().get("traceDataService");

		// TODO Use RipeAtlas to create measurement
		int measurementId = 1717329;

		Map<Probe, TracerouteData> tracedata = tdService
				.loadMeasurementResult(measurementId);

		SinglePointEstimator est = new SinglePointEstimator();

		// This is the US territory range
		est.setRange(new GeoRange(new GeoPoint(24, -125), new GeoDimension(26,
				59)));

		for (Entry<Probe, TracerouteData> entry : tracedata.entrySet()) {
			GeoPoint point = new GeoPoint(entry.getKey().getLatitude(), entry
					.getKey().getLongitude());
			TracerouteData data = entry.getValue();
			if (data.getRoundTripTime().compareTo(new BigDecimal("100")) > 0
					|| data.getRoundTripTime().compareTo(new BigDecimal("5")) < 0) {
				// Too big latency is considered to be a error data
				continue;
			}
			BigDecimal singleTrip = data.getRoundTripTime().divide(
					new BigDecimal("2"), 4, BigDecimal.ROUND_HALF_UP);

			System.err
					.println(MessageFormat.format("Against point {0}", point));
			System.err.println(MessageFormat.format(
					"Single Trip time is {0} ms", singleTrip));
			BigDecimal upperbound = DistanceUtils.upperbound(singleTrip);
			BigDecimal lowerbound = DistanceUtils.lowerbound(singleTrip);

			BigDecimal geoupper = geodiff(point, upperbound).pow(2);
			BigDecimal geolower = geodiff(point, lowerbound).pow(2);

			System.err
					.println(MessageFormat
							.format("Estimated Upper bound is {0} degree^2, lowerbound is {1} degree^2",
									geoupper, geolower));
			DistConstraint d = new DistConstraint(point, null, geoupper,
					geolower);
			est.addConstraint(d);
		}
		// This is the initial points
		GeoPoint result = est.solve();
		
		// Search for boundaries
		
		
		return;
	}

	protected static BigDecimal geodiff(GeoPoint near, BigDecimal distance) {
		BigDecimal latdelta = GeoUtils.distanceToLat(distance, near.latitude);
		BigDecimal longdelta = GeoUtils.distanceToLong(distance, near.latitude);
		return latdelta.add(longdelta).divide(new BigDecimal("2"), 5,
				BigDecimal.ROUND_HALF_UP);
	}
}
