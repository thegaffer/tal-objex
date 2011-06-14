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
package org.talframework.objexj.runtimes.gae;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerObjectCache.CacheState;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.object.writer.BaseObjectWriter.ObjectWriterBehaviour;
import org.talframework.objexj.object.writer.MapObjectWriter;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.KeyRange;
import com.google.appengine.api.datastore.Transaction;

/**
 * Implements the middleware talking to the Google App Engine datastore.
 *
 * @author Tom Spencer
 */
public class GAEMiddleware implements ContainerMiddleware {
    
    /** The type of this container */
    private final String type;
    /** The ID of the container - if null the container is new */
    private String id;
    /** The transaction if we are open */
    private GAETransaction transaction;
    /** The version of the container at the point it was opened */
    private final long version;
    
    /** Holds the root key for this object - or null if the container is new */
    private Key rootKey;
    /** If the container is new, holds the last ID dished out */
    private long lastId = 1;
    
    public GAEMiddleware(String type) {
        this.type = type;
        this.id = null;
        this.transaction = new GAETransaction(type, null, createTransactionId(), 1);
        this.rootKey = null;
        this.version = 1;
    }
    
    public GAEMiddleware(String type, String id) {
        this.type = type;
        
        if( id.endsWith(":trans") ) {
            transaction = GAETransaction.retrieve(id);
            if( transaction == null ) throw new IllegalArgumentException("The transaction id passed in not a valid transaction id: " + id);
            
            this.id = transaction.getId();
            this.version = transaction.getVersion();
        }
        else {
            this.id = id;
            this.transaction = null;
            
            this.version = 1; // FUTURE: Get the current version!?!
        }
        
        // Form the rootKey if an existing container
        if( this.id != null ) {
            int index = this.id.lastIndexOf('/');
            if( index <= 0 ) throw new IllegalArgumentException("The ID of the container cannot be used as GAE container: " + this.id);
            try {
                long idPart = Long.parseLong(this.id.substring(index + 1));
                rootKey = KeyFactory.createKey(type, idPart);
            }
            catch( NumberFormatException e ) {
                throw new IllegalArgumentException("The ID of the container cannot be used as GAE container: " + this.id);
            }
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ContainerObjectCache init(Container container) {
        if( this.type != container.getType() ) throw new IllegalArgumentException("Cannot connect container [" + container + "] to middleware as they have different types: " + this.type);
        
        // TODO: Load all the objects from the cache!!?!
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getContainerId() {
        return id;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(ObjexID id, boolean accurate) {
        if( rootKey == null ) return false;
        else return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ObjexObj loadObject(ObjexObj obj) {
        Key key = GAEKeyUtil.getKey(rootKey, obj);
        
        try {
            Entity entity = getDatastoreService().get(key);
            GAEObjectReader reader = new GAEObjectReader();
            reader.readObject(entity, obj);
        }
        catch( EntityNotFoundException e ) {}
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Map<ObjexID, ObjexObj> loadObjects(ObjexObj... objs) {
        // a. Put into a map of keys to objects
        Map<Key, ObjexObj> objects = new HashMap<Key, ObjexObj>();
        for( ObjexObj obj : objs ) {
            objects.put(GAEKeyUtil.getKey(rootKey, obj), obj);
        }
        
        // b. Find entities for keys and read each one
        Map<Key, Entity> entities = getDatastoreService().get(objects.keySet());
        GAEObjectReader reader = new GAEObjectReader();
        for( Key key : entities.keySet() ) {
            reader.readObject(entities.get(key), objects.get(key));
        }
        
        // c. Produce new map of the entities
        Map<ObjexID, ObjexObj> ret = new HashMap<ObjexID, ObjexObj>();
        for( Key key : entities.keySet() ) {
            ObjexObj obj = objects.get(key);
            if( obj != null ) ret.put(obj.getId(), obj);
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return this.rootKey == null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return transaction != null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        if( transaction != null ) throw new IllegalStateException("Cannot open a container that is already open");
        
        // Generate a new transactionId
        transaction = new GAETransaction(this.type, this.id, createTransactionId(), this.version);
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This implementation asks the datastore service for the next ID or if 
     * the container is new, increments an index.</p>
     * 
     * INVESTIGATE: Is it a lot more efficient to get IDs from datastore say 10 at a time and waste IDs??
     */
    @Override
    public ObjexID getNextObjectId(String type) {
        ObjexID ret = null;
        
        if( rootKey != null ) {
            KeyRange range = getDatastoreService().allocateIds(rootKey, this.type, 1);
            ret = new DefaultObjexID(type, range.getStart().getId());
        }
        else {
            ret = new DefaultObjexID(type, lastId);
            lastId++;
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerListener(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void registerListenerForTransaction(EventListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    /**
     * {@inheritDoc}
     * 
     * For the AppEngine we save the objects into the datastore inside a
     * transaction.
     */
    @Override
    public String save(ContainerObjectCache cache, String status, Map<String, String> header) {
        if( transaction != null ) throw new IllegalStateException("Cannot save if container is not open");
        Transaction trans = null;
        
        try {
            DatastoreService datastoreService = getDatastoreService();
            GAEObjectWriter writer = new GAEObjectWriter();
            trans = datastoreService.beginTransaction();
            
            // a. If new container, create it
            if( rootKey == null ) {
                KeyRange range = datastoreService.allocateIds(type, 1);
                rootKey = range.getStart();
                this.id = type + "/" + rootKey.getId();
            }
            
            // b. Create new objects
            List<Entity> entities = new ArrayList<Entity>();
            Set<ObjexObj> objs = cache.getObjects(CacheState.NEW);
            for( ObjexObj obj : objs ) {
                Key key = GAEKeyUtil.getKey(rootKey, obj);
                Entity e = new Entity(key);
                entities.add(e);
                writer.writeObject(e, obj);
            }
            
            // c. Update existing objects
            objs = cache.getObjects(CacheState.CHANGED);
            for( ObjexObj obj : objs ) {
                Key key = GAEKeyUtil.getKey(rootKey, obj);
                Entity e = new Entity(key); // TODO: Do we need to get first?!?
                entities.add(e);
                writer.writeObject(e, obj);
            }
            
            // d. Get Remove objects keys
            List<Key> keys = new ArrayList<Key>();
            cache.getObjects(CacheState.REMOVED);
            for( ObjexObj obj : objs ) {
                keys.add(GAEKeyUtil.getKey(rootKey, obj));
            }
            
            // e. Actually make the changes & commit
            if( entities.size() > 0 ) datastoreService.put(entities);
            if( keys.size() > 0 ) datastoreService.delete(keys);
            trans.commit();
        }
        catch( Throwable t ) {
            if( trans != null ) trans.rollback();
            throw new RuntimeException("Error committing", t);
        }
        
        // f. Return the containerId
        clear(cache);
        return id;
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>For the app engine we will write the objects to a map and
     * write them out to the mem cache service.</p>
     */
    @Override
    public String suspend(ContainerObjectCache cache) {
        if( transaction != null ) throw new IllegalStateException("Cannot suspend if container is not open");
        
        MapObjectWriter writer = new MapObjectWriter(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES, ObjectWriterBehaviour.INCLUDE_NONPERSISTED);
        
        // a. Write away new objects
        Set<ObjexObj> objs = cache.getObjects(CacheState.NEW);
        for( ObjexObj obj : objs ) {
            Map<String, Object> properties = transaction.getNewObjects().get(obj.getId().toString());
            if( properties == null ) properties = new HashMap<String, Object>();
            writer.writeObject(properties, null, obj);
            transaction.getNewObjects().put(obj.getId().toString(), properties);
        }
        
        // b. Write away changed objects
        objs = cache.getObjects(CacheState.CHANGED);
        for( ObjexObj obj : objs ) {
            Map<String, Object> properties = transaction.getChangedObjects().get(obj.getId().toString());
            if( properties == null ) properties = new HashMap<String, Object>();
            writer.writeObject(properties, null, obj);
            transaction.getChangedObjects().put(obj.getId().toString(), properties);
        }
        
        // c. Write away removed objects
        objs = cache.getObjects(CacheState.REMOVED);
        for( ObjexObj obj : objs ) {
            Map<String, Object> properties = transaction.getRemovedObjects().get(obj.getId().toString());
            if( properties == null ) properties = new HashMap<String, Object>();
            writer.writeObject(properties, null, obj);
            transaction.getRemovedObjects().put(obj.getId().toString(), properties);
        }
        
        // d. Store the transaction away and return the transaction id for future retrieval
        transaction.store();
        return transaction.getTransactionId();
    }
    
    /**
     * {@inheritDoc}
     * 
     * <p>This implementation simply ensures the transaction is removed
     * from the mem cache service (if it exists there).</p>
     */
    @Override
    public void clear(ContainerObjectCache cache) {
        if( transaction != null ) transaction.clear();
        transaction = null;
    }
    
    ///////////////////////////////////////////////////////////////
    // Internals
    
    /**
     * Creates a new transaction id for the container
     */
    private String createTransactionId() {
        // FUTURE: This is a very simple scheme!!!
        return type + "/" + System.currentTimeMillis() + ":trans";
    }
    
    /**
     * Note: We are getting this everytime needed because the suggestion from
     * the docs is that this is lightweight and may not be thread-safe, so 
     * better to get a new one each time.
     * 
     * @return The datastore service
     */
    private DatastoreService getDatastoreService() {
        return DatastoreServiceFactory.getDatastoreService();
    }
}
