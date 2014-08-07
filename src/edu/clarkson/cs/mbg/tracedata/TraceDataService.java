package edu.clarkson.cs.mbg.tracedata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import edu.clarkson.cs.clientlib.common.DefaultHttpService;
import edu.clarkson.cs.clientlib.ripeatlas.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.ProbeAccess;
import edu.clarkson.cs.clientlib.ripeatlas.api.measurement.MeasurementResultResponse;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementResult;
import edu.clarkson.cs.clientlib.ripeatlas.model.Output;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput;
import edu.clarkson.cs.clientlib.ripeatlas.model.TracerouteOutput.TracerouteData;
import edu.clarkson.cs.mbg.tracedata.model.TraceData;

public class TraceDataService extends DefaultHttpService {

	private MeasurementAccess measurementAccess;

	private ProbeAccess probeAccess;

	public Map<Probe, TracerouteData> loadMeasurementResult(int id) {
		Map<Probe, TracerouteData> retval = new HashMap<Probe, TracerouteData>();

		MeasurementResultResponse resultResp = execute(measurementAccess
				.result(String.valueOf(id)));
		List<MeasurementResult> results = resultResp.getResult();
		for (MeasurementResult result : results) {
			Probe probe = execute(probeAccess.get(result.getProbeId()))
					.getResult();
			for (int i = result.getOutputs().size() - 1; i >= 0; i--) {
				Output output = result.getOutputs().get(i);
				if (validStep(output)) {
					TracerouteOutput to = (TracerouteOutput) output;
					TracerouteData sum = summarize(to.getData());
					if (sum.getFrom().equals(result.getDstAddr())) {
						retval.put(probe, sum);
						continue;
					}
				}
			}
		}
		return retval;
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

	protected static TraceData validStart(List<TraceData> exist,
			TracerouteData data) {
		for (int i = exist.size() - 1; i > 0; i--) {
			if (exist.get(i).getRttSource().compareTo(data.getRoundTripTime()) <= 0)
				return exist.get(i);
		}
		return null;
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

		BigDecimal median = null;

		if (datas.size() == 1) {
			median = datas.get(0).getRoundTripTime();
		} else {
			PriorityQueue<BigDecimal> pq = new PriorityQueue<BigDecimal>();
			for (TracerouteData d : datas) {
				pq.add(d.getRoundTripTime());
			}
			int index = (datas.size() - 1) / 2;
			for (int i = 0; i < index; i++) {
				pq.poll();
			}
			median = pq.poll();
		}
		data.setRoundTripTime(median);
		return data;
	}

}
