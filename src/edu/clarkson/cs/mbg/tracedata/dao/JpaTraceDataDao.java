package edu.clarkson.cs.mbg.tracedata.dao;

import javax.persistence.EntityTransaction;

import edu.clarkson.cs.mbg.tracedata.model.TraceData;
import edu.clarkson.cs.persistence.JpaDao;

public class JpaTraceDataDao extends JpaDao<TraceData> {

	public void save(TraceData data) {
		EntityTransaction transaction = getEntityManager().getTransaction();
		transaction.begin();
		getEntityManager().persist(data);
		transaction.commit();
	}
}
