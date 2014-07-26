package edu.clarkson.cs.mbg.tracedata.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.eclipse.persistence.queries.ScrollableCursor;

import edu.clarkson.cs.mbg.tracedata.model.TraceData;
import edu.clarkson.cs.persistence.JpaDao;

public class TraceDataDao extends JpaDao {

	public ScrollableCursor allData() {
		EntityManager em = getEntityManager();
		ScrollableCursor cursor = (ScrollableCursor) em
				.createQuery("select t from TraceData t")
				.setHint("eclipselink.cursor.scrollable", true)
				.getSingleResult();
		return cursor;
	}

	public void save(TraceData data) {
		EntityTransaction transaction = getEntityManager().getTransaction();
		transaction.begin();
		getEntityManager().persist(data);
		transaction.commit();
	}
}
