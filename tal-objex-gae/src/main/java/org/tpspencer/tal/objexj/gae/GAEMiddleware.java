package org.tpspencer.tal.objexj.gae;

import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
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

    /** The ID of the container we are a middlware for */
    private final String id;
    /** The ID of the transaction (if known) */
    private String transactionId;
    
    /** Holds the container (set in the init method) */
    private Container container;
    
    /** The key for the container */
    private final Key rootKey;
    /** The root type for this container */
    private final ContainerBean root;
    
    /** The transaction cache if we are a transaction */
    private TransactionCache cache;
    
    /** Holds the last ID we reserved in persisted root entity */
    private long lastBlockId = -1;
    
    /**
     * Constructs a read-only middleware against an existing container.
     * 
     * @param strategy The strategy for the container
     * @param id The ID of the container
     */
    public GAEMiddleware(ContainerStrategy strategy, String id) {
        this.id = id;
        this.transactionId = null;
        this.rootKey = KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        
        this.root = findExistingRoot(id, true);
    }
    
    /**
     * Constructs a transaction middleware against either an existing
     * transaction or a new transaction against an exist container.
     * 
     * @param strategy The strategy for the container
     * @param id The ID of the container
     * @param transactionId The ID of the transaction (if null, container must exist)
     */
    public GAEMiddleware(ContainerStrategy strategy, String id, String transactionId) {
        this.id = id;
        this.transactionId = transactionId;
        this.rootKey = KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        
        if( transactionId != null ) {
            MemcacheService cache = MemcacheServiceFactory.getMemcacheService();
            GAETransaction trans = (GAETransaction)cache.get(transactionId);
            if( trans != null ) {
                this.root = trans.getContainerBean();
                this.cache = trans.getCache();
            }
            else {
                throw new IllegalArgumentException("The transaction does not exist or has expired: " + transactionId);
            }
        }
        else {
            this.root = findExistingRoot(id, true);
            this.cache = new SimpleTransactionCache();
            this.lastBlockId = this.root.getLastId();
        }
    }
    
    /**
     * Constructs a transaction middleware for a new container.
     * The container is not persisted until saved though.
     * 
     * @param strategy The strategy for the container
     * @param id The ID of the new container
     * @param root The root object to add to transaction
     */
    public GAEMiddleware(ContainerStrategy strategy, String id, ObjexObjStateBean root) {
        this.id = id;
        this.transactionId = null;
        this.rootKey = KeyFactory.createKey(ContainerBean.class.getSimpleName(), id);
        
        // Make sure does not already exist
        if( findExistingRoot(id, false) != null ) {
            throw new IllegalArgumentException("Container already exists");
        }
        
        // Create new root
        this.root = createNewRoot(strategy.getContainerName(), id);
        this.cache = new SimpleTransactionCache();
        
        // Prepare root
        String rootType = strategy.getRootObjectName();
        String rootKey = KeyFactory.createKeyString(this.rootKey, root.getClass().getSimpleName(), 1);
        root.setId(rootKey);
        this.cache.addNewObject(new GAEObjexID(rootType, 1), root);
    }
    
    private ContainerBean findExistingRoot(String id, boolean expectExists) {
        ContainerBean ret = null;
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        try {
            ret = pm.getObjectById(ContainerBean.class, this.rootKey);
            pm.detachCopy(ret);
        }
        catch( JDOObjectNotFoundException e ) {
            if( expectExists ) throw new IllegalArgumentException("The container does not exist: " + id, e);
        }
        finally {
            pm.close();
        }
        
        return ret;
    }
    
    private ContainerBean createNewRoot(String type, String id) {
        ContainerBean ret = new ContainerBean();
        // this.root.setId(rootKey);
        ret.setContainerId(id);
        ret.setType(type);
        // TODO: this.root.setName(name);
        // this.root.setDescription(description);
        // this.root.setStatus(status);
        
        // User and status
        ret.setCreated(new Date());
        UserService userService = UserServiceFactory.getUserService();
        if( userService.isUserLoggedIn() ) ret.setCreator(userService.getCurrentUser().getEmail());
        ret.setLastId(1);
        
        return ret;
    }
    
    /**
     * Ensures the container matches our id
     */
    public void init(Container container) {
        if( !container.getId().equals(id) ) throw new IllegalArgumentException("Container id [" + container.getId() + "] does not match expected id: " + id);
        this.container = container;
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
        if( cache == null && container instanceof EditableContainer ) cache = new SimpleTransactionCache();
        return cache;
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
        if( transactionId == null ) {
            // TODO: Create a new transaction ID !?!
            transactionId = id;
        }
        
        GAETransaction trans = new GAETransaction();
        trans.setId(transactionId);
        trans.setContainerBean(root);
        trans.setCache(cache);
        
        MemcacheService cacheService = MemcacheServiceFactory.getMemcacheService();
        cacheService.put(transactionId, trans);
        
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
