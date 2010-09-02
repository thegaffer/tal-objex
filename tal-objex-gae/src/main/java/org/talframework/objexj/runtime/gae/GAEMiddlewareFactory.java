package org.talframework.objexj.runtime.gae;

import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerMiddlewareFactory;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.impl.SimpleTransactionCache;
import org.talframework.objexj.exceptions.ContainerNotFoundException;
import org.talframework.objexj.exceptions.TransactionNotFoundException;
import org.talframework.objexj.runtime.gae.object.ContainerBean;
import org.talframework.objexj.runtime.gae.persistence.PersistenceManagerFactorySingleton;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * The base Google App Engine middleware factory. This guys
 * job is to provide a middleware that will connect a container
 * up to its datastore in a runtime environment.
 * 
 * @author Tom Spencer
 */
public final class GAEMiddlewareFactory implements ContainerMiddlewareFactory {
    
    /**
     * Determines if the ID represents a transaction or a container
     * and then either loads the transaction or the container. If
     * neither exist then throws a {@link ContainerNotFoundException}
     * or a {@link TransactionNotFoundException}
     */
    public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id) {
        id = stripId(id);
        
	    if( isTransactionId(id) ) {
	        return new GAEMiddleware(strategy, getTransactionBean(id));
	    }
	    else {
	        return new GAEMiddleware(strategy, getContainerBean(id));
	    }
	}
	
    /**
	 * Creates a new transaction around an empty container
	 */
	public ContainerMiddleware createContainer(ContainerStrategy strategy) {
	    GAETransaction trans = new GAETransaction();
	    trans.setId(createTransactionID());
	    trans.setContainerBean(createContainerBean(strategy));
	    trans.setCache(new SimpleTransactionCache());
	    
	    return new GAEMiddleware(strategy, trans);
	}
	
	/**
     * Either gets the existing transaction or creates a new one around
     * the container.
     * 
     * @param strategy The strategy for the container
     * @param id The ID of the transaction or container
     * @return The middleware for the transaction
     * @throws TransactionNotFoundException If the transaction is not found
     * @throws ContainerNotFoundException If the container is not found
     */
    public ContainerMiddleware getTransaction(ContainerStrategy strategy, String id) {
        GAETransaction trans = null;
        id = stripId(id);
        
        // Get existing transaction
        if( isTransactionId(id) ) {
            trans = getTransactionBean(id);
        }
        
        // Create a new transaction
        else {
            trans = new GAETransaction();
            trans.setId(createTransactionID());
            trans.setContainerBean(getContainerBean(id));
            trans.setCache(new SimpleTransactionCache());
        }
        
        return new GAEMiddleware(strategy, trans);
	}
    
    /**
     * Helper to split off the type from the containers ID
     * (if it is present).
     * 
     * @param id The raw ID
     * @return The raw ID
     */
    private static String stripId(String id) {
        int index = id.indexOf('/');
        if( index < 0 ) index = id.indexOf('\\');
        
        if( index >= 0 ) return id.substring(index + 1);
        else return id;
    }
	
    /**
     * Helper to determine if the id represents transaction
     */
	private boolean isTransactionId(String id) {
	    if( id.startsWith("trans:") ) return true;
	    else return false;
    }
    
	/**
	 * Helper to create a new transaction
	 */
    private String createTransactionID() {
        // FUTURE: The generation of a transaction ID below is not really safe!
        return "trans:" + System.currentTimeMillis();
    }
    
    /**
     * Helper to get the container bean
     */
    private ContainerBean getContainerBean(String id) {
        ContainerBean ret = null;
        
        // Determine the key - it may be null
        Key key = null;
        try {
            long i = Long.parseLong(id);
            key = KeyFactory.createKey(ContainerBean.class.getSimpleName(), i);
        }
        catch( NumberFormatException e ) {
            key = KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        }
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        try {
            ret = pm.getObjectById(ContainerBean.class, key);
            pm.detachCopy(ret);
        }
        catch( JDOObjectNotFoundException e ) {
            throw new ContainerNotFoundException(id, e);
        }
        finally {
            pm.close();
        }
        
        return ret;
    }
    
    /**
     * Helper to create a new container bean
     */
    private ContainerBean createContainerBean(ContainerStrategy strategy) {
        ContainerBean ret = new ContainerBean();
        ret.setContainerId(strategy.getContainerId()); // Will be null unless its a store
        ret.setType(strategy.getContainerName());
        
        // User and status
        ret.setCreated(new Date());
        UserService userService = UserServiceFactory.getUserService();
        if( userService.isUserLoggedIn() ) ret.setCreator(userService.getCurrentUser().getEmail());
        
        return ret;
    }
    
    /**
     * Helper to return an existing transaction. 
     */
    private GAETransaction getTransactionBean(String id) {
        MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
        GAETransaction trans = (GAETransaction)cache.get(id);
        if( trans == null ) throw new TransactionNotFoundException(id);
        return trans;
    }
    
    /**
     * Public helper to get a containers key.
     * 
     * @param id The ID of the container
     * @param strip If true then the initial 'ContainerType/' will be stripped
     * @return The containers key
     */
    public static Key getContainerKey(String id, boolean strip) {
        if( strip ) id = stripId(id);
        
        Key key = null;
        try {
            long i = Long.parseLong(id);
            key = KeyFactory.createKey(ContainerBean.class.getSimpleName(), i);
        }
        catch( NumberFormatException e ) {
            key = KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        }
        
        return key;
    }
}
