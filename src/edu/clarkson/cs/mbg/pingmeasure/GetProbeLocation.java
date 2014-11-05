package edu.clarkson.cs.mbg.pingmeasure;

import java.text.MessageFormat;

import edu.clarkson.cs.clientlib.ripeatlas.ProbeService;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.common.BeanContext;
import edu.clarkson.cs.mbg.MBGContextSet;

public class GetProbeLocation {

	public static void main(String[] args) {
		new MBGContextSet().apply();
		ProbeService ps = BeanContext.get().get("probeService");
		int[] probes = { 6062, 6061, 6045, 6072, 6024, 6010, 6065, 13202,
				14754, 3932, 10697, 11025, 11837, 12318 };

		StringBuilder sb = new StringBuilder();
		for (int p : probes) {
			Probe probe = ps.getProbe(p);
			sb.append(MessageFormat.format("'{'x:{0},y:{1},title:\"{2}\"'}',",
					probe.getLatitude(), probe.getLongitude(),
					(probe.isAnchor() ? "Anchor_" : "Probe_") + probe.getId()));
		}
		System.out.println(sb.toString());
	}

}
