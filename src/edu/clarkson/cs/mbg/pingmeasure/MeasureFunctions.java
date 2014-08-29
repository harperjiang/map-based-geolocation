package edu.clarkson.cs.mbg.pingmeasure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.clarkson.cs.clientlib.ripeatlas.api.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.api.measurement.MeasurementCreateResponse;
import edu.clarkson.cs.clientlib.ripeatlas.model.Measurement;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementCreate;
import edu.clarkson.cs.clientlib.ripeatlas.model.MeasurementTarget;
import edu.clarkson.cs.clientlib.ripeatlas.model.PingTarget;
import edu.clarkson.cs.clientlib.ripeatlas.model.ProbeSpec;

public class MeasureFunctions {

	public static void waitTillFinish(Integer[] results, MeasurementAccess ma)
			throws Exception {
		// Wait until the measurements finish
		while (true) {
			boolean stopped = true;
			for (Integer r : results) {
				Measurement m = ma.get(r).execute().getResult();
				if (m.getStatus() != Measurement.Status.Stopped) {
					stopped = false;
					break;
				}
			}
			if (stopped)
				break;
			Thread.sleep(60000);
		}
	}

	static final String anchors = "6062,6061,6045,6072,6024,6010,6065";

	public static ProbeSpec anchors() {
		ProbeSpec ps = new ProbeSpec();
		ps.setRequested(7);
		ps.setType(ProbeSpec.Type.probes.name());
		ps.setValue(anchors);
		return ps;
	}

	static final String probes = "13202,14754,3932,10697,11025,11837,12318";

	public static ProbeSpec probes() {
		ProbeSpec ps = new ProbeSpec();
		ps.setRequested(7);
		ps.setType(ProbeSpec.Type.probes.name());
		ps.setValue(probes);
		return ps;
	}

	public static interface MeasureInfo<T> {
		public String getIp(T input);

		public String getDesc(T input);

		public boolean skip(T input);
	}

	public static <T> void createMeasure(Iterable<T> source,
			MeasurementAccess ma, MeasureInfo<T> info, boolean anchor)
			throws Exception {
		List<T> buffer = new ArrayList<T>();
		for (T cw : source) {
			if (info.skip(cw))
				continue;
			buffer.add(cw);
			if (buffer.size() >= 100) {
				measure(buffer, ma, info, anchor);
				buffer.clear();
			}
		}

		if (!buffer.isEmpty()) {
			measure(buffer, ma, info, anchor);
			buffer.clear();
		}
	}

	public static <T> void measure(List<T> targets, MeasurementAccess ma,
			MeasureInfo<T> info, boolean anchor) throws Exception {
		MeasurementCreate mc = new MeasurementCreate();
		List<MeasurementTarget> mts = new ArrayList<MeasurementTarget>();
		Set<String> exist = new HashSet<String>();
		for (T t : targets) {
			String ipAddress = info.getIp(t);
			if (exist.contains(ipAddress))
				continue;
			exist.add(ipAddress);
			PingTarget target = new PingTarget();
			target.setAf(4);
			target.setDescription(info.getDesc(t));
			target.setOneoff(true);
			target.setTarget(ipAddress);
			target.setPackets(5);
			target.setPublicc(false);
			mc.setTargets(mts);

			mts.add(target);
		}
		ProbeSpec ps = null;
		if (anchor)
			ps = anchors();
		else {
			ps = probes();
		}
		List<ProbeSpec> pss = new ArrayList<ProbeSpec>();
		pss.add(ps);

		mc.setProbes(pss);

		MeasurementCreateResponse mcr = ma.create(mc).execute();

		while (mcr.getError() != null) {
			mcr = ma.create(mc).execute();
			Thread.sleep(5000);
		}
		Integer[] results = mcr.getResult();
		MeasureFunctions.waitTillFinish(results, ma);

	}
}
