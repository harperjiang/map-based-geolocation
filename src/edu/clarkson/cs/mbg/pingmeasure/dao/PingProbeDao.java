package edu.clarkson.cs.mbg.pingmeasure.dao;

import java.util.List;

import edu.clarkson.cs.mbg.pingmeasure.model.PingProbe;
import edu.clarkson.cs.persistence.EntityDao;

public interface PingProbeDao extends EntityDao<PingProbe> {

	List<PingProbe> findByIp(String ip);
}
