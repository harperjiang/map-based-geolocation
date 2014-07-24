package edu.clarkson.cs.mbg;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Environment {

	public static EntityManager em;

	static {
		em = Persistence.createEntityManagerFactory("mbg")
				.createEntityManager();
	}
}
