package org.tpspencer.tal.objexj.gae.persistence;

import javax.jdo.JDOHelper; 
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory; 

/**
 * The single instance of the PersistenceManagerFactory
 * 
 * @author Tom Spencer
 */
public class PersistenceManagerFactorySingleton {
	private static final PersistenceManagerFactory INSTANCE = 
		JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	private PersistenceManagerFactorySingleton() {
	}
	
	public static PersistenceManager getManager() {
		return INSTANCE.getPersistenceManager();
	}
}
