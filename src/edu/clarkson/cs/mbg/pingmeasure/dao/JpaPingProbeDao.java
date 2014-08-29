package edu.clarkson.cs.mbg.pingmeasure.dao;

import java.util.List;

import edu.clarkson.cs.mbg.pingmeasure.model.PingProbe;
import edu.clarkson.cs.persistence.JpaEntityDao;

public class JpaPingProbeDao extends JpaEntityDao<PingProbe> implements
		PingProbeDao {

	@Override
	public List<PingProbe> findByIp(String ip) {
		return getEntityManager()
				.createQuery(
						"select p from PingProbe p where p.ipAddress = :ip",
						PingProbe.class).setParameter("ip", ip).getResultList();
	}

}
