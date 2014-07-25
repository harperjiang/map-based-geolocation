package edu.clarkson.cs.mbg.map.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import edu.clarkson.cs.mbg.Environment;
import edu.clarkson.cs.mbg.map.model.Section;

public class JpaRoadDao implements RoadDao {

	protected EntityManager getEm() {
		return Environment.em;
	}

	public void save(Section road) {
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
