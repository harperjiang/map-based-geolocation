package edu.clarkson.cs.mbg.pingmeasure.dao;

import java.math.BigDecimal;
import java.util.List;

import edu.clarkson.cs.mbg.pingmeasure.model.Landmark;
import edu.clarkson.cs.persistence.EntityDao;

public interface LandmarkDao extends EntityDao<Landmark> {

	public List<Landmark> findInRange(BigDecimal latFrom, BigDecimal latTo,
			BigDecimal longFrom, BigDecimal longTo);
}
