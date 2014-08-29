package edu.clarkson.cs.mbg.pingmeasure;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.ProbeService;
import edu.clarkson.cs.clientlib.ripeatlas.api.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.model.Probe;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.pingmeasure.MeasureFunctions.MeasureInfo;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingProbeDao;

public class CreatePingProbeMeasure {

	public static void main(String[] args) throws Exception {
		createProbeMeasure();
	}

	static void createAnchorMeasure() throws Exception {
		new MBGContextSet().apply();

		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		ProbeService ps = BeanContext.get().get("probeService");
		final PingAnchorDao paDao = BeanContext.get().get("pingAnchorDao");
		MeasureInfo<Probe> mip = new MeasureInfo<Probe>() {
			@Override
			public String getDesc(Probe input) {
				return "PingProbe_Anchor_" + input.getId();
			}

			@Override
			public String getIp(Probe input) {
				return input.getAddressV4();
			}

			@Override
			public boolean skip(Probe input) {
				if (StringUtils.isEmpty(input.getAddressV4())) {
					return true;
				}
				if (!CollectionUtils.isEmpty(paDao.findByIp(input
						.getAddressV4()))) {
					return true;
				}
				return false;
			}
		};

		List<Probe> usProbes = ps.findByCountry("US");

		MeasureFunctions.createMeasure(usProbes, ma, mip, true);
	}

	static void createProbeMeasure() throws Exception {
		new MBGContextSet().apply();

		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		ProbeService ps = BeanContext.get().get("probeService");
		final PingProbeDao paDao = BeanContext.get().get("pingProbeDao");
		MeasureInfo<Probe> mip = new MeasureInfo<Probe>() {
			@Override
			public String getDesc(Probe input) {
				return "PingProbe_Probe_" + input.getId();
			}

			@Override
			public String getIp(Probe input) {
				return input.getAddressV4();
			}

			@Override
			public boolean skip(Probe input) {
				if (StringUtils.isEmpty(input.getAddressV4())) {
					return true;
				}
				if (!CollectionUtils.isEmpty(paDao.findByIp(input
						.getAddressV4()))) {
					return true;
				}
				return false;
			}
		};

		List<Probe> usProbes = ps.findByCountry("US");

		MeasureFunctions.createMeasure(usProbes, ma, mip, false);
	}
}
