package edu.clarkson.cs.mbg;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.ProbeAccess;
import edu.clarkson.cs.clientlib.ripeatlas.RipeAtlasContextSet;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.mbg.geo.GeoPoint;

public class FindProbeInRange {

	public static void main(String[] args) throws Exception {
		GeoPoint point = new GeoPoint(new BigDecimal("41.47"), new BigDecimal(
				"-72.745"));
		Double latmax = point.latitude.doubleValue() + 5;
		Double latmin = point.latitude.doubleValue() - 5;
		Double longmax = point.longitude.doubleValue() + 5;
		Double longmin = point.longitude.doubleValue() - 5;

		new RipeAtlasContextSet().apply();

		List<Probe> result = new ArrayList<Probe>();

		ProbeAccess pa = BeanContext.get().get("probeAccess");
		int start = 0;
		boolean success = false;
		while (true) {
			List<Probe> probes = pa.list(start, 500, (Object[]) null).execute()
					.getResult();
			if (null == probes || probes.size() == 0)
				break;
			for (Probe p : probes) {
				if (p.isPublicc() && p.getLatitude() <= latmax
						&& p.getLatitude() >= latmin
						&& p.getLongitude() <= longmax
						&& p.getLongitude() >= longmin) {
					result.add(p);
					if (result.size() > 30) {
						success = true;
						break;
					}
				}
			}
			if (success)
				break;
			start += 500;
		}
		for (Probe p : result) {
			System.out.print(String.valueOf(p.getId()) + ",");
		}
		return;
	}
}
