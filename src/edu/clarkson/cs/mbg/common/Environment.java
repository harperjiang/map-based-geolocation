package edu.clarkson.cs.mbg.common;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class Environment {

	private Environment() {
		super();

		// Prepare JPA environment
		em = Persistence.createEntityManagerFactory("mbg")
				.createEntityManager();
	}

	private static Environment instance;
	static {
		instance = new Environment();
	}

	public static Environment getEnvironment() {
		return instance;
	}

	private EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}
}
