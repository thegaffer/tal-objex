package org.tpspencer.tal.objexj.gae;

import java.util.Iterator;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.gae.object.ContainerBean;
import org.tpspencer.tal.objexj.gae.persistence.PersistenceManagerFactorySingleton;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * The unified middleware for Google App Engine environments.
 * 
 * @author Tom Spencer
 */
public final class GAEMiddleware implements TransactionMiddleware {

    /** The root bean for this container */
    private final ContainerBean root;
    
    /** Holds the ID strategy for the container */
    private final ObjexIDStrategy idStrategy;
    
    /** Holds the container (set in the init method) */
    private Container container;
    
    /** The ID of the transaction (if known, if not and we are in a transaction this is generated in suspend) */
    private String transactionId;
    /** The transaction cache if we are a transaction (if not we are read-only) */
    private TransactionCache cache;
    
    /**
     * Constructs a middleware against an existing container.
     * 
     * @param strategy The strategy for the container
     * @param id The ID of the container
     */
    public GAEMiddleware(ContainerStrategy strategy, ContainerBean bean) {
        this.root = bean;
        
        this.transactionId = null;
        this.cache = null;
        
        idStrategy = GAEAllocateObjexIDStrategy.getInstance();
    }
    
    /**
     * Constructs a new GAEMiddleware a transaction. This may be a
     * new container (containerId is null) or a new transaction over 
     * an existing container.
     * 
     * @param strategy The strategy of the container we are serving
     * @param transaction The transaction
     */
    public GAEMiddleware(ContainerStrategy strategy, GAETransaction transaction) {
        this.root = transaction.getContainerBean();
        
        this.transactionId = transaction.getId();
        this.cache = transaction.getCache();
        
        if( this.root.getId() == null ) idStrategy = new GAENewObjexIDStrategy(transaction.getLastId());
        else idStrategy = GAEAllocateObjexIDStrategy.getInstance();
    }
    
    /**
     * Internal helper to ensure the middleware is initialised
     */
    private void checkInitialised() {
        if( this.container == null ) throw new IllegalStateException("The middleware is not initialisaed, it must be initialised before use");
    }
    
    /**
     * Internal helper to ensure the middleware is initialised
     */
    private void checkTransaction() {
        checkInitialised();
        if( this.transactionId == null ) throw new IllegalStateException("The middleware is not in a transaction so write operations are not possible");
    }
    
    private Key getKey(Key rootKey, String type, ObjexID id) {
        Object val = id.getId();
        if( val instanceof Long ) return KeyFactory.createKey(rootKey, type, ((Long)val).longValue());
        else return KeyFactory.createKey(rootKey, type, val.toString());
    }
    
    private String getKeyString(Key rootKey, String type, ObjexID id) {
        Object val = id.getId();
        if( val instanceof Long ) return KeyFactory.createKeyString(rootKey, type, ((Long)val).longValue());
        else return KeyFactory.createKeyString(rootKey, type, val.toString());
    }
    
    ////////////////////////////////////////////////////
    // Standard Middleware
    
    /**
     * Ensures the container matches our id
     */
    public void init(Container container) {
        this.container = container;
    }
    
    /**
     * Returns the full container ID only if it has
     * been assigned an ID - if not returns null
     */
    public String getContainerId() {
        return root.getFullContainerId();
    }
    
    /**
     * Helper to physically load an object from the
     * datastore given its ID
     * 
     * @param type The type of class expected
     * @param id The ID to perform
     * @return The persisted state of the object
     */
    public Object loadObject(Class<?> type, ObjexID id) {
        checkInitialised();
        
        Object ret = null;
        
        // a. Try the cache first
        if( cache != null ) {
            ret = cache.findObject(id);
        }
        
        // b. Otherwise load if we are a real container
        if( ret == null && !isNew() ) {
            PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
            try {
                Key k = getKey(root.getId(), type.getSimpleName(), id);
                ret = pm.getObjectById(type, k);
                if( ret != null ) pm.detachCopy(ret); // Detach so any changes are not persisted
            }
            finally {
                pm.close();
            }
            
            // SUGGEST: Should we add to the cache at this point or perhaps record version?
        }
        
        return ret;
    }
    
    ////////////////////////////////////////////////////
    // Transaction Middleware
    
    /**
     * @return True if the container is a new one
     */
    public boolean isNew() {
        return this.root.getId() == null;
    }
    
    /**
     * Simply returns the ID strategy
     */
    public ObjexIDStrategy getIdStrategy() {
        return idStrategy;
    }
    
    /**
     * Gets the existing transaction cache from memory, or
     * creates a new one
     */
    public TransactionCache getCache() {
        return cache;
    }
    
    public String save() {
        checkTransaction();
        
        // FUTURE: Update container name, desc and status
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        Transaction tx = pm.currentTransaction();
        
        try {
            tx.begin();
            
            // Make the root persistent (only if changed!!)
            Key rootKey = root.getId();
            if( rootKey == null ) {
                // Creating a doc
                if( root.getContainerId() == null ) {
                    rootKey = GAEAllocateObjexIDStrategy.getInstance().createContainerId(root.getContainerId());
                    root.setContainerId(Long.toString(rootKey.getId()));
                }
                
                // Creating a store
                else {
                    rootKey = GAEAllocateObjexIDStrategy.getInstance().createContainerId(root.getContainerId());
                }
                
                root.setId(rootKey);
            }
            pm.makePersistent(root);
            
            // Save or create objects
            if( cache.getNewObjects() != null ) {
                Map<ObjexID, ObjexObjStateBean> newObjects = cache.getNewObjects();
                Iterator<ObjexID> it = newObjects.keySet().iterator();
                while( it.hasNext() ) {
                    ObjexID id = it.next();
                    ObjexObjStateBean bean = newObjects.get(id);
                    String k = getKeyString(rootKey, bean.getClass().getSimpleName(), id);
                    bean.init(k);
                }
                pm.makePersistentAll(cache.getNewObjects().values());
            }
            if( cache.getUpdatedObjects() != null ) pm.makePersistentAll(cache.getUpdatedObjects().values());
            if( cache.getRemovedObjects() != null ) pm.deletePersistentAll(cache.getRemovedObjects().values());
            
            tx.commit();
        }
        finally {
            if( tx.isActive() ) tx.rollback();
            pm.close();
        }
        
        return root.getFullContainerId();
    }
    
    public String suspend() {
        checkTransaction();
        
        // Create and save the transaction
        GAETransaction trans = new GAETransaction();
        trans.setId(transactionId);
        trans.setContainerBean(root);
        trans.setCache(cache);
        
        if( idStrategy instanceof GAENewObjexIDStrategy ) {
            trans.setLastId(((GAENewObjexIDStrategy)idStrategy).getLastId());
        }
        
        MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
        cacheService.put(transactionId, trans);
        
        return transactionId;
    }
    
    public void clear() {
        if( transactionId != null ) {
            MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
            cacheService.delete(transactionId);
        }
    }
}
