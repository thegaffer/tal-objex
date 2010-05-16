package org.tpspencer.tal.objexj.gae;

import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.SimpleTransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionMiddleware;
import org.tpspencer.tal.objexj.gae.object.ContainerBean;
import org.tpspencer.tal.objexj.gae.persistence.PersistenceManagerFactorySingleton;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * The unified middleware for Google App Engine environments.
 * 
 * @author Tom Spencer
 */
public class GAEMiddleware implements TransactionMiddleware {

    /** Set at construction to indicate if we expect the container to already exist */
    private final boolean expectExists;
    /** Holds the container */
    private Container container;
    
    /** The key for the container */
    private Key rootKey;
    /** The root type for this container */
    private ContainerBean root;
    
    /** Holds the last ID we reserved in persisted root entity */
    private long lastBlockId = -1;
    
    public GAEMiddleware(boolean expectExists) {
        this.expectExists = expectExists;
    }

    public void init(Container container) {
        this.container = container;
        
        // Generate key for the container
        this.rootKey = KeyFactory.createKey(ContainerBean.class.getSimpleName(), container.getId());
        
        // Make sure container exists or otherwise already
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        try {
            this.root = pm.getObjectById(ContainerBean.class, this.rootKey);
            pm.detachCopy(this.root);
            this.lastBlockId = this.root.getLastId();
        }
        catch( JDOObjectNotFoundException e ) {
            if( expectExists ) throw new IllegalArgumentException("The container does not exist: " + container.getId(), e);
            
            this.root = new ContainerBean();
            // this.root.setId(rootKey);
            this.root.setContainerId(container.getId());
            this.root.setType(container.getType());
            // TODO: this.root.setName(name);
            // this.root.setDescription(description);
            // this.root.setStatus(status);
            
            // User and status
            this.root.setCreated(new Date());
            UserService userService = UserServiceFactory.getUserService();
            if( userService.isUserLoggedIn() ) this.root.setCreator(userService.getCurrentUser().getEmail());
            this.root.setLastId(0);
        }
        finally {
            pm.close();
        }
        
        if( this.root == null ) throw new IllegalArgumentException("Cannot find existing container in data store: " + container.getId());
    }
    
    /**
     * @return True if the container is a new one
     */
    public boolean isNew() {
        return root.getId() == null;
    }
    
    /**
     * Casts the GAEObjexID or converts as necc
     */
    public ObjexID convertId(Object id) {
        return GAEObjexID.getId(id);
    }
    
    /**
     * Simply casts to a GAEObjexID and returns the type
     */
    public String getObjectType(ObjexID id) {
        return GAEObjexID.getId(id).getType();
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
        if( container == null ) throw new IllegalStateException("Cannot load an object from an uninitialised middleware");
        
        // If new container cannot load anything!
        if( root.getId() == null ) return null;
        
        Object ret = null;
        GAEObjexID gaeId = GAEObjexID.getId(id);
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        try {
            String k = KeyFactory.createKeyString(rootKey, type.getSimpleName(), gaeId.getId());
            ret = pm.getObjectById(type, k);
            if( ret != null ) pm.detachCopy(ret); // Detach so any changes are not persisted
        }
        finally {
            pm.close();
        }
        
        return ret;
    }
    
    public ObjexID createNewId(String type, ObjexObjStateBean bean) {
        long newId = this.root.getLastId();
        
        // Get block of id's if required
        if( this.root.getId() != null && (lastBlockId < 1 || lastBlockId >= newId) ) {
            PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
            Transaction tx = pm.currentTransaction();
            try {
                tx.begin();
                
                // FUTURE: I hate this mechanism - revisit later
                ContainerBean root = pm.getObjectById(ContainerBean.class, rootKey);
                newId = root.getLastId();
                this.lastBlockId = root.getLastId() + 10; // TODO: Make '10' configurable
                root.setLastId(this.lastBlockId);
                this.root.setLastId(this.lastBlockId);
                
                tx.commit();
            }
            finally {
                if( tx.isActive() ) tx.rollback();
                pm.close();
            }
        }
        
        // Increment on ID
        newId++;
        if( this.root.getId() == null ) this.root.setLastId(newId); // Otherwise the block id is fine!
        
        // Set real key on the object
        String k = KeyFactory.createKeyString(rootKey, bean.getClass().getSimpleName(), newId);
        bean.setId(k);
        return new GAEObjexID(type, newId);
    }
    
    /**
     * Gets the existing transaction cache from memory, or
     * creates a new one
     */
    public TransactionCache getCache() {
        TransactionCache ret = null;
        
        String transactionId = ((EditableContainer)container).getTransactionId();
        if( transactionId != null ) {
            MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
            ret = (TransactionCache)cache.get(transactionId);
            
            if( ret == null ) {
                // TODO: This is probably an error to return back to say cache expired!!
                ret = new SimpleTransactionCache();
            }
        }
        
        else {
            ret = new SimpleTransactionCache();
        }
        
        return ret;
    }
    
    public void save(TransactionCache cache) {
        // TODO: Update container name, desc and status
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        Transaction tx = pm.currentTransaction();
        
        try {
            tx.begin();
            
            // Make the root persistent (only if changed!!)
            if( root.getId() == null ) root.setId(rootKey);
            pm.makePersistent(root);
            
            // Save or create objects
            if( cache.getNewObjects() != null ) pm.makePersistentAll(cache.getNewObjects().values());
            if( cache.getUpdatedObjects() != null ) pm.makePersistentAll(cache.getUpdatedObjects().values());
            if( cache.getRemovedObjects() != null ) pm.deletePersistentAll(cache.getRemovedObjects().values());
            
            tx.commit();
        }
        finally {
            if( tx.isActive() ) tx.rollback();
            pm.close();
        }
    }
    
    public String suspend(TransactionCache cache) {
        String transactionId = ((EditableContainer)container).getTransactionId();
        if( transactionId == null ) {
            // TODO: Create a new transaction ID !?!
            transactionId = container.getId();
        }
        
        MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
        cacheService.put(transactionId, cache);
        
        return transactionId;
    }
    
    public void clear(TransactionCache cache) {
        String transactionId = ((EditableContainer)container).getTransactionId();
        if( transactionId != null ) {
            MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
            cacheService.delete(transactionId);
        }
    }
}
