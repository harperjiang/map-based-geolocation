package edu.clarkson.cs.mbg.tracedata;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.clarkson.cs.clientlib.common.DefaultHttpService;
import edu.clarkson.cs.clientlib.lang.Callback;
import edu.clarkson.cs.clientlib.lang.sort.Medianer;
import edu.clarkson.cs.clientlib.ripeatlas.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.ProbeAccess;
import edu.clarkson.cs.clientlib.ripeatlas.api.measurement.MeasurementResultResponse;
import edu.clarkson.cs.clientlib.ripeatlas.model.Measurement;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementTarget;
import edu.clarkson.cs.clientlib.ripeatlas.model.Output;
import edu.clarkson.cs.clientlib.ripeatlas.model.PingOutput;
import edu.clarkson.cs.clientlib.ripeatlas.model.PingTarget;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.clientlib.ripeatlas.model.ProbeSpec;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput.TracerouteData;
import edu.clarkson.cs.mbg.topology.model.Graph;
import edu.clarkson.cs.mbg.topology.model.Node;
import edu.clarkson.cs.mbg.topology.model.Path;

public class TraceDataService extends DefaultHttpService {

	private MeasurementAccess measurementAccess;

	private ProbeAccess probeAccess;

	public Map<Probe, List<TracerouteData>> loadTraceResult(int id) {
		Map<Probe, List<TracerouteData>> retval = new HashMap<Probe, List<TracerouteData>>();

		MeasurementResultResponse resultResp = execute(measurementAccess
				.result(id));
		List<MeasurementResult> results = resultResp.getResult();
		for (MeasurementResult result : results) {
			Probe probe = execute(probeAccess.get(result.getProbeId()))
					.getResult();

			List<TracerouteData> datas = new ArrayList<TracerouteData>();

			for (int i = 0; i < result.getOutputs().size(); i++) {
				Output output = result.getOutputs().get(i);
				if (validStep(output)) {
					TracerouteOutput to = (TracerouteOutput) output;
					if (to.getHop() == 255) {
						// If this trace use 255 as its max hop, there is some
						// problems and this result should be discarded
						datas.clear();
						break;
					}
					TracerouteData sum = summarize(to.getData());
					datas.add(sum);
				}
			}
			if (datas.size() != 0) {
				// Only if the last one reaches the destination
				TracerouteData last = datas.get(datas.size() - 1);
				if (last.getFrom().equals(result.getDstAddr()))
					retval.put(probe, datas);
			}
		}
		return retval;
	}

	/**
	 * 
	 * @param measurementId
	 * @return Map<Probe, [Median]>
	 */
	public Map<Probe, BigDecimal> loadPingResult(int measurementId) {
		Map<Probe, BigDecimal> retval = new HashMap<Probe, BigDecimal>();

		MeasurementResultResponse resultResp = execute(measurementAccess
				.result(measurementId));
		List<MeasurementResult> results = resultResp.getResult();

		for (MeasurementResult result : results) {
			Probe probe = execute(probeAccess.get(result.getProbeId()))
					.getResult();

			List<BigDecimal> values = new ArrayList<BigDecimal>();
			for (Output op : result.getOutputs()) {
				if (op instanceof PingOutput) {
					values.add(((PingOutput) op).getRtt());
				}
			}
			if (values.size() != 0) {
				BigDecimal median = new Medianer<BigDecimal>().median(values);
				retval.put(probe, median);
			}
		}

		return retval;
	}

	public Graph getTraceGraph(int measurementId) {
		Map<Probe, List<TracerouteData>> measure = loadTraceResult(measurementId);

		Graph graph = new Graph();

		for (Entry<Probe, List<TracerouteData>> entry : measure.entrySet()) {
			Probe probe = entry.getKey();
			List<TracerouteData> traces = entry.getValue();
			TracerouteData header = traces.get(0);

			Graph smallg = new Graph();
			boolean validg = true;

			smallg.addPath(new Path(new Node(probe.getAddressV4(), probe),
					new Node(header.getFrom()), header.getRoundTripTime()));

			for (int i = 0; i < traces.size() - 1; i++) {
				TracerouteData current = traces.get(i);
				TracerouteData next = traces.get(i + 1);

				// Don't add graphs that contains loop
				if (current.getFrom().equals(next.getFrom())) {
					validg = false;
					break;
				}
				Node nextNode = new Node(next.getFrom());
				if (smallg.getNodes().contains(nextNode)) {
					validg = false;
					break;
				}
				smallg.addPath(new Path(new Node(current.getFrom()), nextNode,
						next.getRoundTripTime().subtract(
								current.getRoundTripTime())));
			}
			if (validg)
				graph.merge(smallg);

		}
		return graph;
	}

	static final BigDecimal PING_THRESHOLD = new BigDecimal("5");

	/**
	 * Use random probes with ping to compare two IP addresses.
	 * 
	 * @param ip1
	 * @param ip2
	 * @return
	 */
	public void similar(final String ip1, final String ip2,
			final Callback<Boolean> callback) {
		new Thread() {
			public void run() {
				MeasurementCreate mc = new MeasurementCreate();

				List<MeasurementTarget> mts = new ArrayList<MeasurementTarget>();

				PingTarget pingmt1 = new PingTarget();
				pingmt1.setAf(4);
				pingmt1.setDescription(MessageFormat.format(
						"ComparePing_{0}_{1}", ip1, ip2));
				pingmt1.setOneoff(true);
				pingmt1.setPublicc(false);
				pingmt1.setType("ping");
				pingmt1.setPackets(5);
				pingmt1.setTarget(ip1);
				mts.add(pingmt1);

				PingTarget pingmt2 = new PingTarget();
				pingmt2.setAf(4);
				pingmt2.setDescription(MessageFormat.format(
						"ComparePing_{1}_{0}", ip1, ip2));
				pingmt2.setOneoff(true);
				pingmt2.setPublicc(false);
				pingmt2.setType("ping");
				pingmt2.setPackets(5);
				pingmt2.setTarget(ip1);
				mts.add(pingmt2);

				List<ProbeSpec> pss = new ArrayList<ProbeSpec>();
				ProbeSpec ps = new ProbeSpec();
				ps.setRequested(10);
				ps.setType(ProbeSpec.Type.country.name());
				ps.setValue("US");
				pss.add(ps);

				// Create Ping measurement
				mc.setTargets(mts);
				mc.setProbes(pss);

				Integer[] ids = execute(getMeasurementAccess().create(mc))
						.getResult();

				// Wait for RIPE Atlas to finish

				while (true) {
					// Check the result every minute
					try {
						Thread.sleep(60000);
					} catch (Exception e1) {
						throw new RuntimeException(e1);
					}
					int stopped = 0;
					for (int id : ids) {
						Measurement mr = execute(getMeasurementAccess().get(id))
								.getResult();
						if (mr.getStatus() == Measurement.Status.Stopped)
							stopped += 1;
						else
							break;
					}
					if (stopped == ids.length)
						break;
				}

				BigDecimal sum = comparePing(ids[0], ids[1]);

				callback.done(sum.compareTo(PING_THRESHOLD) <= 0);
			}
		}.start();
	}

	public BigDecimal comparePing(int id1, int id2) {
		Map<Probe, BigDecimal> ping1 = loadPingResult(id1);
		Map<Probe, BigDecimal> ping2 = loadPingResult(id2);

		BigDecimal sum = BigDecimal.ZERO;
		List<BigDecimal> vals = new ArrayList<BigDecimal>();
		for (Entry<Probe, BigDecimal> e : ping1.entrySet()) {
			BigDecimal diff = e.getValue().subtract(ping2.get(e.getKey()))
					.abs();
			// System.out.println(MessageFormat.format(
			// "{0} vs {1}, difference {2}", e.getValue(),
			// ping2.get(e.getKey()), diff));
			vals.add(diff);
		}

		for (BigDecimal val : vals) {
			sum = sum.add(val);
		}
		sum = sum.divide(new BigDecimal(ping1.size()), 5,
				BigDecimal.ROUND_HALF_UP);
		return sum;
	}

	private boolean validStep(Output output) {
		if (!(output instanceof TracerouteOutput))
			return false;
		TracerouteOutput to = (TracerouteOutput) output;
		if (to.getData().size() == 0)
			return false;
		TracerouteData data = to.getData().get(0);
		if (IPUtils.isPrivateIp(data.getFrom()))
			return false;
		return true;
	}

	private TracerouteData summarize(List<TracerouteData> datas) {
		TracerouteData data = new TracerouteData();
		data.setRoundTripTime(BigDecimal.ZERO);
		data.setFrom(datas.get(0).getFrom());

		List<BigDecimal> values = new ArrayList<BigDecimal>();

		for (TracerouteData d : datas) {
			values.add(d.getRoundTripTime());
		}
		BigDecimal median = new Medianer<BigDecimal>().median(values);

		data.setRoundTripTime(median);
		return data;
	}

	public MeasurementAccess getMeasurementAccess() {
		return measurementAccess;
	}

	public void setMeasurementAccess(MeasurementAccess measurementAccess) {
		this.measurementAccess = measurementAccess;
	}

	public ProbeAccess getProbeAccess() {
		return probeAccess;
	}

	public void setProbeAccess(ProbeAccess probeAccess) {
		this.probeAccess = probeAccess;
	}
}
