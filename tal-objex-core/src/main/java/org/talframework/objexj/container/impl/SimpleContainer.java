/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.tpspencer.tal.objexj.container.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.util.Assert;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.RootObjexObj;
import org.tpspencer.tal.objexj.ValidationRequest;
import org.tpspencer.tal.objexj.container.ContainerMiddleware;
import org.tpspencer.tal.objexj.container.ContainerStrategy;
import org.tpspencer.tal.objexj.container.InternalContainer;
import org.tpspencer.tal.objexj.container.TransactionCache;
import org.tpspencer.tal.objexj.container.TransactionCache.ObjectRole;
import org.tpspencer.tal.objexj.events.Event;
import org.tpspencer.tal.objexj.events.EventHandler;
import org.tpspencer.tal.objexj.exceptions.ContainerInvalidException;
import org.tpspencer.tal.objexj.exceptions.EventHandlerNotFoundException;
import org.tpspencer.tal.objexj.exceptions.ObjectNotFoundException;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.query.Query;
import org.tpspencer.tal.objexj.query.QueryRequest;
import org.tpspencer.tal.objexj.query.QueryResult;

/**
 * This class is the default form of container and handles
 * it both in a transaction and when read-only.
 *
 * @author Tom Spencer
 */
public final class SimpleContainer implements InternalContainer {
    
    /** Holds the strategy for this container */
    private final ContainerStrategy strategy;
    
    /** Holds the ID of the container */
    private String id;
    /** Holds the runtime middleware for this container */
    private final ContainerMiddleware middleware;

    /** Member holds a cache of all objects served by container */
    private Map<ObjexID, ObjexObj> objexObjCache;
    /** The cache of all objects in the transaction */
    private TransactionCache transactionCache;
    
    /**
     * Standard constructor for the container.
     * 
     * @param id The ID of the container
     * @param strategy The general strategy for the container
     * @param idStrategy The specific runtime ID strategy to use
     */
    public SimpleContainer(
            String id, 
            ContainerStrategy strategy, 
            ContainerMiddleware middleware,
            boolean open) {
        Assert.notNull(strategy, "The container strategy must be present");
        Assert.notNull(middleware, "The container middleware must be present");
        
        this.id = id;
        this.strategy = strategy;
        this.middleware = middleware;
        if( open ) this.transactionCache = middleware.getCache();
        
        middleware.init(this);
    }
    
    /**
     * Helper to determine if we are in a transaction or not.
     * This is based on whether we have a transaction cache.
     */
    private void checkInTransaction() {
        if( transactionCache == null ) throw new IllegalStateException("The container [" + this.id + "] is not open so changes cannot be made");
    }
    
    /**
     * Returns the ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Returns the strategies name
     */
    public String getType() {
        return strategy.getContainerName();
    }
    
    /**
     * Obtains the root object by getting the root id from
     * the container strategy.
     * 
     * @return The root object
     */
    public ObjexObj getRootObject() {
        ObjexID rootId = strategy.getRootObjectID();
        return getObject(rootId);
    }
    
    /**
     * Gets the object by the ID and then the expected
     * behvaiour.
     */
    public <T> T getObject(Object id, Class<T> expected) {
        ObjexObj obj = getObject(id);
        return obj != null ? obj.getBehaviour(expected) : null;
    }
    
    /**
     * Gets the object from the following sources:
     * <ul>
     * <li>The cache of previously served objects</li>
     * <li>The transaction cache (if there is one)</li>
     * <li>The middleware</li>
     * 
     * <p>Any object found is added to the object cache 
     * for future use.</p>
     */
    public ObjexObj getObject(Object id) {
        if( id == null ) return null; // Just protect from a stupid call!!
        
        ObjexID realId = DefaultObjexID.getId(id);
        if( realId == null ) throw new IllegalArgumentException("ID passed in does not appear to be valid: " + id);
        
        // See if we already have it
        ObjexObj ret = getLocalObject(realId);
        
        // Go to middleware if neccessary
        if( ret == null ) {
            ObjectStrategy objectStrategy = strategy.getObjectStrategy(realId.getType());
            
            ObjexObjStateBean state = middleware.loadObject(objectStrategy.getStateClass(), realId);
            if( state != null ) {
                ret = objectStrategy.getObjexObjInstance(this, DefaultObjexID.getId(state.getParentId()), realId, state);
                if( objexObjCache == null ) objexObjCache = new HashMap<ObjexID, ObjexObj>();
                objexObjCache.put(realId, ret);
            }
            else {
                throw new ObjectNotFoundException(this.id, realId.toString());
            }
        }
        
        return ret;
    }
    
    /**
     * Helper to get an object, but only if held locally.
     * This method will not call the middleware.
     * 
     * @param id The ID of the object
     * @return The object if known in container
     */
    private ObjexObj getLocalObject(ObjexID id) {
        ObjexObj ret = null;
        
        // a. See if we already hold it as an object
        ret = objexObjCache != null ? objexObjCache.get(id) : null;
        if( ret != null ) return ret;
        
        // b. See if in cache, if so create ObjexObj around it and cache that
        ObjexObjStateBean state = transactionCache != null ? transactionCache.findObject(id, null) : null;
        if( state != null ) {
            ObjectStrategy objectStrategy = strategy.getObjectStrategy(id.getType());
            ret = objectStrategy.getObjexObjInstance(this, DefaultObjexID.getId(state.getParentId()), id, state);
            if( objexObjCache == null ) objexObjCache = new HashMap<ObjexID, ObjexObj>();
            objexObjCache.put(id, ret);
        }
        
        return ret;
    }
    
    /**
     * Simply gets each object on the list
     * 
     * FUTURE: Work out objects not held locally and get in one op
     */
    public List<ObjexObj> getObjectList(List<? extends Object> ids) {
        if( ids == null ) return null;
        
        List<ObjexObj> ret = new ArrayList<ObjexObj>();
        Iterator<? extends Object> it = ids.iterator();
        while( it.hasNext() ) {
            ret.add(getObject(it.next()));
        }
        
        return ret;
    }
    
    /**
     * Gets the objects using the normal method and creates
     * a new list of the expected element by calling
     * getBehaviour on the objects.
     * 
     * @throws ClassCastException If any object is not of the expected type
     */
    public <T> List<T> getObjectList(List<? extends Object> ids, Class<T> expectedElement) {
        List<ObjexObj> objs = getObjectList(ids);
        List<T> ret = objs != null ? new ArrayList<T>(objs.size()) : null;
        
        if( objs != null ) {
            Iterator<ObjexObj> it = objs.iterator();
            while( it.hasNext() ) {
                ret.add(it.next().getBehaviour(expectedElement));
            }
        }
        
        return ret;
    }
    
    /**
     * Simply gets each object on the list
     * 
     * FUTURE: Work out objects not held locally and get in one op
     */
    public Map<? extends Object, ObjexObj> getObjectMap(Map<? extends Object, ? extends Object> ids) {
        if( ids == null ) return null;
        
        Map<Object, ObjexObj> ret = new HashMap<Object, ObjexObj>();
        Iterator<? extends Object> it = ids.keySet().iterator();
        while( it.hasNext() ) {
            Object k = it.next();
            Object ref = ids.get(k);
            ret.put(k, getObject(ref));
        }
        
        return ret;
    }
    
    /**
     * Gets the objects using the normal method and creates
     * a new map of the expected element by calling
     * getBehaviour on the objects.
     * 
     * @throws ClassCastException If any object is not of the expected type
     */
    public <T> Map<? extends Object, T> getObjectMap(Map<? extends Object, ? extends Object> ids, Class<T> expectedElement) {
        Map<? extends Object, ObjexObj> objs = getObjectMap(ids);
        Map<Object, T> ret = objs != null ? new HashMap<Object, T>(objs.size()) : null;
        
        if( objs != null ) {
            Iterator<? extends Object> it = objs.keySet().iterator();
            while( it.hasNext() ) {
                Object key = it.next();
                ObjexObj obj = objs.get(key);
                ret.put(key, obj != null ? obj.getBehaviour(expectedElement) : null);
            }
        }
        
        return ret;
    }
    
    /**
     * Retrieves the query from the container strategy, extracts
     * the object strategy for the type the query runs on and
     * executes the query.
     */
    public QueryResult executeQuery(QueryRequest request) {
        Query query = strategy.getQuery(request.getName());
        return query.execute(this, strategy, request);
    }
    
    /**
     * Simply delegates to the event processor by name.
     * 
     * @throws EventHandlerNotFoundException If the event is unknown
     */
    public void processEvent(Event event) {
        EventHandler handler = strategy.getEventHandler(event.getEventName());
        handler.execute(this, event);
    }
    
    /**
     * Simply asks the middleware to open if not already open.
     * An exception is not thrown if already open.
     * 
     * @return The editable container interface to this container
     */
    public Container openContainer() {
        if( transactionCache == null ) transactionCache = middleware.open();
        // TODO: Should we throw error if already open!?!
        return this;
    }
    
    /**
     * Asks the middleware if the container is new
     */
    public boolean isNew() {
        return middleware.isNew();
    }
    
    /**
     * Determines based on the transaction cache presence
     */
    public boolean isOpen() {
        return transactionCache != null;
    }

    /**
     * Validates all new/changed objects inside container
     */
    public ValidationRequest validate() {
        ObjexObj obj = getRootObject();
        RootObjexObj root = (obj instanceof RootObjexObj) ? (RootObjexObj)obj : null; 
        
        // Validate
        ValidationRequest request = ContainerValidator.validate(this, transactionCache, root != null ? root.getErrors() : null);
        
        // Save with root if applicable
        request = root != null ? root.processValidation(request) : request;
        
        return request;
    }
    
    /**
     * Simply returns the stored validation errors
     */
    public ValidationRequest getErrors() {
        ObjexObj obj = getRootObject();
        if( obj instanceof RootObjexObj ) return ((RootObjexObj)obj).getErrors();
        return null;
    }
    
    /**
     * Validates the container, assigns any permanent IDs and
     * then saves via the middleware. 
     */
    public String saveContainer() {
        checkInTransaction();
        
        ObjexObj obj = getRootObject();
        RootObjexObj root = null;
        if( obj instanceof RootObjexObj ) root = (RootObjexObj)obj;
        
        // a. Validate and make sure we can save
        ValidationRequest request = validate();
        if( (root != null && !root.canSave(request)) || 
                (root == null && request.hasErrors()) ) {
            throw new ContainerInvalidException(id, request);
        }
        
        // b. Assign perm IDs
        assignPermanentIds();
    
        // c. Save
        String status = root != null ? root.getStatus() : null;
        Map<String, String> header = root != null ? root.getHeader() : null;
        id = middleware.save(status, header);
        objexObjCache = null;
        transactionCache = null;
        return getId();
    }
    
    /**
     * Removes any caches and suspends the middleware.
     */
    public String suspend() {
        checkInTransaction();
        
        objexObjCache = null;
        transactionCache = null;
        return middleware.suspend();
    }
    
    /**
     * Removes any caches and clears the middleware.
     */
    public void closeContainer() {
        objexObjCache = null;
        transactionCache = null;
        middleware.clear();
    }
    
    ///////////////////////////////////////////////
    // Internal Specific
    
    /**
     * Simply defers to the cache to answer the question
     */
    public ObjectRole getObjectRole(ObjexID id) {
        return transactionCache != null ? transactionCache.getObjectRole(id) : ObjectRole.NONE;
    }
    
    /**
     * Called by children to add themselves to the transaction when
     * they are about to change. If already in the transaction this
     * is a no-op.
     */
    public void addObjectToTransaction(ObjexObj obj, ObjexObjStateBean state) {
        checkInTransaction();
        
        ObjectRole role = transactionCache.getObjectRole(obj.getId());
        
        switch(role) {
        case NONE:
            transactionCache.addObject(ObjectRole.UPDATED, obj.getId(), state);
            state.setEditable();
            break;
            
        case REMOVED:
            // TODO: Throw an exception - cannot edit removed!?!
            break;
        }
    }
    
    /**
     * Creates the object and adds both it and the parent object
     * to the transaction.
     */
    public ObjexObj newObject(ObjexObj obj, ObjexObjStateBean state, String type) {
        checkInTransaction();
        
        ObjectStrategy objectStrategy = strategy.getObjectStrategy(type);
        Class<? extends ObjexObjStateBean> stateClass = objectStrategy.getStateClass();
        
        // a. Create the temp ID
        ObjexIDStrategy idStrategy = objectStrategy.getIdStrategy();
        if( idStrategy == null ) idStrategy = middleware.getIdStrategy();
        ObjexID newId = idStrategy.createId(this, stateClass, type, null);
        ObjexID parentId = obj != null ? obj.getId() : null;
        
        // b. Create the state object & add to transaction
        ObjexObjStateBean newState = createStateBean(objectStrategy.getStateClass(), parentId);
        newState.setEditable();
        transactionCache.addObject(ObjectRole.NEW, newId, newState);
        
        // c. Create ObjexObj, add it to cache and return it
        ObjexObj ret = objectStrategy.getObjexObjInstance(this, parentId, newId, newState);
        if( objexObjCache == null ) objexObjCache = new HashMap<ObjexID, ObjexObj>();
        objexObjCache.put(newId, ret);
        return ret;
    }
    
    public void removeObject(ObjexObj obj, ObjexObjStateBean state) {
        checkInTransaction();
        
        transactionCache.addObject(ObjectRole.REMOVED, obj.getId(), state);
    }
    
    /**
     * Helper to create a state bean given its class.
     * The bean will be writable straight away
     * 
     * @param cls The class of the state bean
     * @param parentId The parentId (if any)
     * @return The new state bean
     */
    private ObjexObjStateBean createStateBean(Class<? extends ObjexObjStateBean> cls, ObjexID parentId) {
        try {
            ObjexObjStateBean bean = cls.newInstance();
            bean.create(parentId);
            return bean;
        }
        catch( RuntimeException e ) {
            throw e;
        }
        catch( Exception e ) {
            throw new IllegalArgumentException("Cannot create state bean: " + cls, e);
        }
    }
    
    ///////////////////////////////////////////////
    // Internal
    
    /**
     * Helper to assign permanent IDs to any new objects that
     * have temporary IDs
     */
    private void assignPermanentIds() {
        Map<ObjexID, ObjexObjStateBean> newObjects = transactionCache.getObjects(ObjectRole.NEW);
        if( newObjects != null ) {
            Map<ObjexID, ObjexID> tempRefs = null;
            Iterator<ObjexID> it = newObjects.keySet().iterator();
            while( it.hasNext() ) {
                ObjexID temp = it.next();
                if( temp.isTemp() ) {
                    if( tempRefs == null ) tempRefs = new HashMap<ObjexID, ObjexID>();
                    ObjexID realId = null; // TODO: Get the real ID
                    tempRefs.put(temp, realId);
                }
            }
            
            // Now clean up any references
            if( tempRefs != null ) {
                it = tempRefs.keySet().iterator();
                while( it.hasNext() ) {
                    ObjexID temp = it.next();
                    ObjexObjStateBean bean = newObjects.get(temp);
                    newObjects.remove(temp);
                    newObjects.put(tempRefs.get(temp), bean);
                }
                
                it = newObjects.keySet().iterator();
                while( it.hasNext() ) {
                    newObjects.get(it.next()).updateTemporaryReferences(tempRefs);
                }
                
                Map<ObjexID, ObjexObjStateBean> updatedObjects = transactionCache.getObjects(ObjectRole.UPDATED);
                if( updatedObjects != null ) {
                    it = updatedObjects.keySet().iterator();
                    while( it.hasNext() ) {
                        updatedObjects.get(it.next()).updateTemporaryReferences(tempRefs);
                    }
                }
            }
        }
    }
}
