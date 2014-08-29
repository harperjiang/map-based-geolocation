package edu.clarkson.cs.mbg.pingmeasure;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.api.MeasurementAccess;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.CityWebsiteDao;
import edu.clarkson.cs.mbg.map.model.CityWebsite;
import edu.clarkson.cs.mbg.pingmeasure.MeasureFunctions.MeasureInfo;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingProbeDao;

public class CreatePingCityMeasure {

	public static void main(String[] args) throws Exception {
		// createAnchorMeasure();
		createProbeMeasure();
	}

	static void createAnchorMeasure() throws Exception {
		new MBGContextSet().apply();

		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		CityWebsiteDao cwDao = BeanContext.get().get("cityWebsiteDao");
		final PingAnchorDao paDao = BeanContext.get().get("pingAnchorDao");

		MeasureInfo<CityWebsite> mic = new MeasureInfo<CityWebsite>() {
			@Override
			public String getDesc(CityWebsite input) {
				return "PingAnchor_City_" + input.getId();
			}

			@Override
			public String getIp(CityWebsite input) {
				return input.getIpAddress();
			}

			@Override
			public boolean skip(CityWebsite input) {
				if (StringUtils.isEmpty(input.getIpAddress())) {
					return true;
				}
				if (!CollectionUtils.isEmpty(paDao.findByIp(input
						.getIpAddress()))) {
					return true;
				}
				return false;
			}
		};

		MeasureFunctions.createMeasure(cwDao.all(), ma, mic, true);
	}

	static void createProbeMeasure() throws Exception {
		new MBGContextSet().apply();

		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		CityWebsiteDao cwDao = BeanContext.get().get("cityWebsiteDao");
		final PingProbeDao ppDao = BeanContext.get().get("pingProbeDao");

		MeasureInfo<CityWebsite> mic = new MeasureInfo<CityWebsite>() {
			@Override
			public String getDesc(CityWebsite input) {
				return "PingProbe_City_" + input.getId();
			}

			@Override
			public String getIp(CityWebsite input) {
				return input.getIpAddress();
			}

			@Override
			public boolean skip(CityWebsite input) {
				if (StringUtils.isEmpty(input.getIpAddress())) {
					return true;
				}
				if (!CollectionUtils.isEmpty(ppDao.findByIp(input
						.getIpAddress()))) {
					return true;
				}
				return false;
			}
		};

		MeasureFunctions.createMeasure(cwDao.all(), ma, mic, false);
	}
}
