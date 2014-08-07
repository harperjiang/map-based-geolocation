package edu.clarkson.cs.mbg;

import java.awt.Point;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.clientlib.csdp.CSDPException;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput.TracerouteData;
import edu.clarkson.cs.mbg.geo.GeoDimension;
import edu.clarkson.cs.mbg.geo.GeoPoint;
import edu.clarkson.cs.mbg.geo.GeoRange;
import edu.clarkson.cs.mbg.geo.GeoUtils;
import edu.clarkson.cs.mbg.map.DistanceUtils;
import edu.clarkson.cs.mbg.opt.BoundarySearcher;
import edu.clarkson.cs.mbg.opt.BoundarySearcher.Behavior;
import edu.clarkson.cs.mbg.opt.DistConstraint;
import edu.clarkson.cs.mbg.opt.SinglePointEstimator;
import edu.clarkson.cs.mbg.tool.drawrange.DrawPoint;
import edu.clarkson.cs.mbg.tool.drawrange.DrawRangeFrame;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;

public class MBGMain {

	// This is the US territory range
	static final GeoRange territory = new GeoRange(new GeoPoint(24, -125),
			new GeoDimension(26, 59));

	public static void main(String[] args) {
		System.loadLibrary("jcsdp");
		new MBGContextSet().apply();

		TraceDataService tdService = BeanContext.get().get("traceDataService");

		// TODO Use RipeAtlas to create measurement
		int measurementId = 1717512;

		Map<Probe, TracerouteData> tracedata = tdService
				.loadMeasurementResult(measurementId);

		final SinglePointEstimator est = new SinglePointEstimator();

		est.setRange(territory);

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

			BigDecimal upperbound = DistanceUtils.upperbound(singleTrip);
			BigDecimal lowerbound = DistanceUtils.lowerbound(singleTrip);

			BigDecimal geoupper = geodiff(point, upperbound).pow(2);
			BigDecimal geolower = geodiff(point, lowerbound).pow(2);

			DistConstraint d = new DistConstraint(point, null, geoupper,
					geolower);
			est.addConstraint(d);
		}
		// This is the initial points
		GeoPoint result = est.solve();

		return;
	}

	protected static BigDecimal geodiff(GeoPoint near, BigDecimal distance) {
		BigDecimal latdelta = GeoUtils.distanceToLat(distance, near.latitude);
		BigDecimal longdelta = GeoUtils.distanceToLong(distance, near.latitude);
		return latdelta.add(longdelta).divide(new BigDecimal("2"), 5,
				BigDecimal.ROUND_HALF_UP);
	}

	protected static void searchBoundary(final SinglePointEstimator est,
			final GeoPoint result) {
		// Search for boundaries
		BoundarySearcher<GeoPoint> boundarySearcher = new BoundarySearcher<GeoPoint>(
				new Behavior<GeoPoint>() {

					@Override
					public List<GeoPoint> expand(GeoPoint current) {
						List<GeoPoint> res = new ArrayList<GeoPoint>();
						res.add(new GeoPoint(current.latitude
								.add(BigDecimal.ONE), current.longitude));
						res.add(new GeoPoint(current.latitude,
								current.longitude.add(BigDecimal.ONE)));
						res.add(new GeoPoint(current.latitude
								.subtract(BigDecimal.ONE), current.longitude));
						res.add(new GeoPoint(current.latitude,
								current.longitude.subtract(BigDecimal.ONE)));
						return res;
					}

					@Override
					public boolean test(GeoPoint point) {
						GeoRange newRange = GeoUtils.overlap(new GeoRange(
								point, new GeoDimension(1, 1)), territory);
						if (null == newRange)
							return false;
						est.setRange(newRange);
						try {
							est.solve();
							return true;
						} catch (CSDPException e) {
							return false;
						}
					}

				});
		boundarySearcher.setStartPoint(new GeoPoint(result.latitude.setScale(0,
				BigDecimal.ROUND_FLOOR), result.longitude.setScale(0,
				BigDecimal.ROUND_FLOOR)));
		boundarySearcher.search();

		DrawRangeFrame frame = new DrawRangeFrame();
		frame.setVisible(false);
		for (GeoPoint boundary : boundarySearcher.getBoundary()) {
			frame.getDrPanel()
					.getItems()
					.add(new DrawPoint(new Point(boundary.longitude.intValue(),
							boundary.latitude.intValue())));
		}
		frame.setVisible(true);
	}
}
