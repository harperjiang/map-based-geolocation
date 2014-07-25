package edu.clarkson.cs.mbg.common;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public abstract class JpaDao<T extends EntityObject> {

	protected EntityManager getEntityManager() {
		return Environment.getEnvironment().getEntityManager();
	}

	public void save(T object) {
		EntityTransaction t = getEntityManager().getTransaction();
		t.begin();

		if (object.getId() == null) {
			getEntityManager().persist(object);
		} else {
			getEntityManager().merge(object);
		}

		t.commit();
	}

}
