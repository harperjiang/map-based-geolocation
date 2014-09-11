package edu.clarkson.cs.mbg.pingmeasure.dao;

import java.math.BigDecimal;
import java.util.List;

import edu.clarkson.cs.mbg.pingmeasure.model.Landmark;
import edu.clarkson.cs.persistence.JpaEntityDao;

public class JpaLandmarkDao extends JpaEntityDao<Landmark> implements
		LandmarkDao {

	@Override
	public List<Landmark> findInRange(BigDecimal latFrom, BigDecimal latTo,
			BigDecimal longFrom, BigDecimal longTo) {
		String query = "select l from Landmark l where (l.latitude between :latFrom and :latTo) and (l.longitude between :longFrom and :longTo)";
		return getEntityManager().createQuery(query, Landmark.class)
				.setParameter("latFrom", latFrom).setParameter("latTo", latTo)
				.setParameter("longFrom", longFrom)
				.setParameter("longTo", longTo).getResultList();
	}

}
