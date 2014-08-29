package edu.clarkson.cs.mbg;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import edu.clarkson.cs.clientlib.ipinfo.IPInfoContextSet;
import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.lang.ContextSet;
import edu.clarkson.cs.clientlib.ripeatlas.RipeAtlasContextSet;
import edu.clarkson.cs.clientlib.ripeatlas.api.MeasurementAccess;
import edu.clarkson.cs.clientlib.ripeatlas.api.ProbeAccess;
import edu.clarkson.cs.mbg.map.dao.JpaCityDao;
import edu.clarkson.cs.mbg.map.dao.JpaCityWebsiteDao;
import edu.clarkson.cs.mbg.map.dao.JpaRoadDao;
import edu.clarkson.cs.mbg.map.dao.JpaUniversityDao;
import edu.clarkson.cs.mbg.pingmeasure.dao.JpaPingAnchorDao;
import edu.clarkson.cs.mbg.pingmeasure.dao.JpaPingProbeDao;
import edu.clarkson.cs.mbg.topology.GraphService;
import edu.clarkson.cs.mbg.tracedata.TraceDataService;

public class MBGContextSet implements ContextSet {

	@Override
	public void apply() {
		new RipeAtlasContextSet().apply();
		new IPInfoContextSet().apply();

		EntityManager em = Persistence.createEntityManagerFactory("mbg")
				.createEntityManager();

		BeanContext.get().put("entityManager", em);

		JpaRoadDao roadDao = new JpaRoadDao();
		roadDao.setEntityManager(em);
		BeanContext.get().put("roadDao", roadDao);

		JpaCityDao cityDao = new JpaCityDao();
		cityDao.setEntityManager(em);
		BeanContext.get().put("cityDao", cityDao);

		JpaUniversityDao universityDao = new JpaUniversityDao();
		universityDao.setEntityManager(em);
		BeanContext.get().put("universityDao", universityDao);

		JpaPingAnchorDao pingAnchorDao = new JpaPingAnchorDao();
		pingAnchorDao.setEntityManager(em);
		BeanContext.get().put("pingAnchorDao", pingAnchorDao);

		JpaPingProbeDao pingProbeDao = new JpaPingProbeDao();
		pingProbeDao.setEntityManager(em);
		BeanContext.get().put("pingProbeDao", pingProbeDao);

		JpaCityWebsiteDao cityWebsiteDao = new JpaCityWebsiteDao();
		cityWebsiteDao.setEntityManager(em);
		BeanContext.get().put("cityWebsiteDao", cityWebsiteDao);
		// Beans

		TraceDataService traceDataService = new TraceDataService();
		MeasurementAccess ma = BeanContext.get().get("measurementAccess");
		traceDataService.setMeasurementAccess(ma);
		ProbeAccess pa = BeanContext.get().get("probeAccess");
		traceDataService.setProbeAccess(pa);

		BeanContext.get().put("traceDataService", traceDataService);
		BeanContext.get().put("graphService", new GraphService());
	}

}
