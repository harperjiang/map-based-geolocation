package edu.clarkson.cs.mbg;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import edu.clarkson.cs.clientlib.lang.BeanContext;
import edu.clarkson.cs.clientlib.lang.ContextSet;
import edu.clarkson.cs.mbg.map.dao.JpaRoadDao;
import edu.clarkson.cs.mbg.tracedata.dao.JpaTraceDataDao;

public class MBGContextSet implements ContextSet {

	@Override
	public void apply() {

		EntityManager em = Persistence.createEntityManagerFactory("mbg")
				.createEntityManager();

		JpaRoadDao roadDao = new JpaRoadDao();
		roadDao.setEntityManager(em);
		BeanContext.get().put("roadDao", roadDao);

		JpaTraceDataDao traceDataDao = new JpaTraceDataDao();
		traceDataDao.setEntityManager(em);
		BeanContext.get().put("traceDataDao", traceDataDao);
	}

}
