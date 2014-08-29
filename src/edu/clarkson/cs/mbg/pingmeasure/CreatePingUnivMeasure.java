package edu.clarkson.cs.mbg.pingmeasure;

import org.apache.commons.collections4.CollectionUtils;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.ripeatlas.api.MeasurementAccess;
import edu.clarkson.cs.mbg.MBGContextSet;
import edu.clarkson.cs.mbg.map.dao.UniversityDao;
import edu.clarkson.cs.mbg.map.model.University;
import edu.clarkson.cs.mbg.pingmeasure.MeasureFunctions.MeasureInfo;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.dao.PingProbeDao;

public class CreatePingUnivMeasure {

	public static void main(String[] args) throws Exception {
		createProbeMeasure();
	}

	public static void createAnchorMeasure() throws Exception {
		new MBGContextSet().apply();

		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		UniversityDao univDao = BeanContext.get().get("universityDao");
		final PingAnchorDao paDao = BeanContext.get().get("pingAnchorDao");

		MeasureInfo<University> miu = new MeasureInfo<University>() {
			@Override
			public String getDesc(University input) {
				return "PingAnchor_Univ_" + input.getId();
			}

			@Override
			public String getIp(University input) {
				return input.getIpAddress();
			}

			@Override
			public boolean skip(University input) {
				if (!CollectionUtils.isEmpty(paDao.findByIp(input
						.getIpAddress()))) {
					return true;
				}
				return false;
			}
		};

		MeasureFunctions.createMeasure(univDao.all(), ma, miu, true);
	}

	public static void createProbeMeasure() throws Exception {
		new MBGContextSet().apply();

		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		UniversityDao univDao = BeanContext.get().get("universityDao");
		final PingProbeDao paDao = BeanContext.get().get("pingProbeDao");

		MeasureInfo<University> miu = new MeasureInfo<University>() {
			@Override
			public String getDesc(University input) {
				return "PingProbe_Univ_" + input.getId();
			}

			@Override
			public String getIp(University input) {
				return input.getIpAddress();
			}

			@Override
			public boolean skip(University input) {
				if (!CollectionUtils.isEmpty(paDao.findByIp(input
						.getIpAddress()))) {
					return true;
				}
				return false;
			}
		};

		MeasureFunctions.createMeasure(univDao.all(), ma, miu, false);
	}

}
