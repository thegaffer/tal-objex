package org.talframework.objexj.runtime.gae.persistence;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

/**
 * The single instance of the PersistenceManagerFactory
 * 
 * TODO: Create an interface, using singleton injection technique and add other platform elements like getting a new ID
 * 
 * @author Tom Spencer
 */
public final class PersistenceManagerFactorySingleton {
    private static final PersistenceManagerFactory INSTANCE = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PersistenceManagerFactorySingleton() {
    }

    public static PersistenceManager getManager() {
        return INSTANCE.getPersistenceManager();
    }
}
