package edu.clarkson.cs.mbg.pingmeasure.dao;

import java.util.List;

import edu.clarkson.cs.mbg.pingmeasure.model.PingAnchor;
import edu.clarkson.cs.persistence.EntityDao;

public interface PingAnchorDao extends EntityDao<PingAnchor> {

	List<PingAnchor> findByIp(String ip);
}
