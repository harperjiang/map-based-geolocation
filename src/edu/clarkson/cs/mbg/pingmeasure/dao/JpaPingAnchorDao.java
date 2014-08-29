package edu.clarkson.cs.mbg.pingmeasure.dao;

import java.util.List;

import edu.clarkson.cs.mbg.pingmeasure.model.PingAnchor;
import edu.clarkson.cs.persistence.JpaEntityDao;

public class JpaPingAnchorDao extends JpaEntityDao<PingAnchor> implements
		PingAnchorDao {

	@Override
	public List<PingAnchor> findByIp(String ip) {
		return getEntityManager()
				.createQuery(
						"select p from PingAnchor p where p.ipAddress = :ip",
						PingAnchor.class).setParameter("ip", ip)
				.getResultList();
	}

}
