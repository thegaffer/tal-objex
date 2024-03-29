/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.runtime.gae;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.url;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.ObjexIDStrategy;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.container.TransactionCache.ObjectRole;
import org.talframework.objexj.container.impl.SimpleTransactionCache;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.runtime.gae.event.GAEEventListener;
import org.talframework.objexj.runtime.gae.object.ContainerBean;
import org.talframework.objexj.runtime.gae.persistence.PersistenceManagerFactorySingleton;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * The unified middleware for Google App Engine environments.
 * 
 * @author Tom Spencer
 */
public final class GAEMiddleware implements ContainerMiddleware {

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
    
    /** The standard listeners (if any) */
    private List<EventListener> standardListners;
    /** The registered listeners (if any) */
    //private List<GAEEventListener> registeredListeners;
    /** The transaction specific listeners (if any) */
    private List<EventListener> transactionListeners;
    
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
        standardListners = strategy.getStandardListeners();
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
        standardListners = strategy.getStandardListeners();
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
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        checkInitialised();
        
        ObjexObjStateBean ret = null;
        
        // a. Try the cache first
        if( cache != null ) {
            ret = cache.findObject(id, null);
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
     * Creates a new cache as neccessary
     */
    public TransactionCache open() {
        if( cache == null ) cache = new SimpleTransactionCache();
        return cache;
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
    
    public String save(String status, Map<String, String> header) {
        checkTransaction();
        
        PersistenceManager pm = PersistenceManagerFactorySingleton.getManager();
        Transaction tx = pm.currentTransaction();
        
        boolean created = false;
        String oldStatus = root.getStatus();
        String newStatus = status;
        
        // Work out the listeners
        List<EventListener> listeners = getApplicableListeners(created, oldStatus, newStatus);
        
        try {
            tx.begin();
            
            Map<ObjexID, ObjexObjStateBean> newObjects = cache.getObjects(ObjectRole.NEW);
            Map<ObjexID, ObjexObjStateBean> updatedObjects = cache.getObjects(ObjectRole.UPDATED);
            Map<ObjexID, ObjexObjStateBean> removedObjects = cache.getObjects(ObjectRole.REMOVED);
            
            // Make the root persistent (only if changed!!)
            Key rootKey = root.getId();
            if( rootKey == null ) {
                created = true;
                
                // Creating a doc
                if( root.getContainerId() == null ) {
                    rootKey = GAEAllocateObjexIDStrategy.getInstance().createContainerId(root.getContainerId());
                    root.setContainerId(Long.toString(rootKey.getId()));
                    
                    // Reserve all the IDs create thus far - allocate does not protect from manual entries
                    if( newObjects != null && newObjects.size() > 0 ) {
                        DatastoreServiceFactory.getDatastoreService().allocateIds(rootKey, container.getType(), newObjects.size() + 1);
                    }
                }
                
                // Creating a store
                else {
                    rootKey = GAEAllocateObjexIDStrategy.getInstance().createContainerId(root.getContainerId());
                }
                
                root.setId(rootKey);
            }
            pm.makePersistent(root);
            
            // Save or create objects
            if( newObjects != null ) {
                Iterator<ObjexID> it = newObjects.keySet().iterator();
                while( it.hasNext() ) {
                    ObjexID id = it.next();
                    ObjexObjStateBean bean = newObjects.get(id);
                    String k = getKeyString(rootKey, bean.getClass().getSimpleName(), id);
                    bean.preSave(k);
                }
                pm.makePersistentAll(newObjects.values());
            }
            if( updatedObjects != null ) pm.makePersistentAll(updatedObjects.values());
            if( removedObjects != null ) pm.deletePersistentAll(removedObjects.values());
            
            tx.commit();
        }
        finally {
            if( tx.isActive() ) tx.rollback();
            pm.close();
        }
        
        // TODO: Should be inside the transaction
        if( listeners != null ) sendListeners(listeners, oldStatus, header);
        
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
    
    public void registerListener(EventListener listener) {
        checkTransaction();
        root.addListener(listener);
    }
    
    /**
     * Adds the given listener to the ones for this transaction.
     * There must be a transaction open.
     */
    public void registerListenerForTransaction(EventListener listener) {
        checkTransaction();
        if( transactionListeners == null ) transactionListeners = new ArrayList<EventListener>();
        transactionListeners.add(listener);
    }
 
    /**
     * Helper to get all the applicable listeners for this transaction
     * based on whether the container is created, its old status and
     * its new status.
     * 
     * @param created If true container is being created
     * @param oldStatus The status the container was in
     * @param newStatus The status the container is now in
     * @return The list of applicable listeners or null
     */
    private List<EventListener> getApplicableListeners(boolean created, String oldStatus, String newStatus) {
        // Quit if no listeners
        if( standardListners == null && root.getRegisteredListeners() == null && transactionListeners == null ) return null;
        
        List<EventListener> ret = new ArrayList<EventListener>();
        
        Iterator<EventListener> it = standardListners != null ? standardListners.iterator() : null;
        while( it != null && it.hasNext() ) {
            EventListener e = it.next();
            if( isListenerApplicable(e, created, oldStatus, newStatus) ) ret.add(e);
        }
        
        List<GAEEventListener> registeredListeners = root.getRegisteredListeners();
        Iterator<GAEEventListener> it2 = registeredListeners != null ? registeredListeners.iterator() : null;
        while( it2 != null && it2.hasNext() ) {
            EventListener e = it2.next();
            if( isListenerApplicable(e, created, oldStatus, newStatus) ) ret.add(e);
        }
        
        it = transactionListeners != null ? transactionListeners.iterator() : null;
        while( it != null && it.hasNext() ) {
            EventListener e = it.next();
            if( isListenerApplicable(e, created, oldStatus, newStatus) ) ret.add(e);
        }
        
        return ret.size() > 0 ? ret : null;
    }
    
    /**
     * Helper to determine if the specific listener is interested.
     */
    private boolean isListenerApplicable(EventListener listener, boolean created, String oldStatus, String newStatus) {
        if( listener.isOnEdit() ) return true;
        if( created && listener.isOnCreation() ) return true;
        
        boolean stateChange = oldStatus != null ? oldStatus.equals(newStatus) : newStatus != null;
        
        if( listener.isOnStateChange() && stateChange ) return true;
        if( newStatus != null && listener.getInterestedStates() != null ) {
            String[] states = listener.getInterestedStates();
            for( int i = 0 ; i < states.length ; i++ ) {
                if( states[i].equals(newStatus) ) return true;
            }
        }
        
        // We are not interested in this listener
        return false;
    }
    
    /**
     * Call to send out an event to all listeners.
     * 
     * TODO: Put inside a transaction
     * 
     * @param listeners The listeners to send to
     * @param oldState The old state of the container before transaction
     */
    private void sendListeners(List<EventListener> listeners, String oldState, Map<String, String> header) {
        Queue defaultQueue = QueueFactory.getDefaultQueue();
        
        Iterator<EventListener> it = listeners.iterator();
        while( it.hasNext() ) {
            EventListener e = it.next();
            
            // Get queue
            Queue queue = defaultQueue;
            if( e.getChannel() != null ) {
                queue = QueueFactory.getQueue(e.getChannel());
            }
            
            TaskOptions task = url("/_containerEvent");
            task.header("X-Objex-Container", e.getContainer());
            task.header("X-Objex-SourceContainer", root.getFullContainerId());
            task.header("X-Objex-Event", e.getEventProcessor());
            if( root.getStatus() != null ) task.header("X-Objex-State", root.getStatus());
            if( oldState != null ) task.header("X-Objex-OldState", oldState);

            // Add payload if there is one
            if( header != null ) {
                Iterator<String> it2 = header.keySet().iterator();
                while( it2.hasNext() ) {
                    String name = it2.next();
                    task.param(name, header.get(name));
                }
            }
            
            // Send event
            queue.add(task);
        }
    }
}
