package edu.clarkson.cs.mbg.road;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import edu.clarkson.cs.mbg.Environment;
import edu.clarkson.cs.mbg.road.model.Road;

public class JpaRoadDao implements RoadDao {

	protected EntityManager getEm() {
		return Environment.em;
	}

	public void save(Road road) {
		EntityTransaction t = getEm().getTransaction();
		t.begin();

		if (road.getId() == null) {
			getEm().persist(road);
		} else {
			getEm().merge(road);
		}

		t.commit();
	}
}
