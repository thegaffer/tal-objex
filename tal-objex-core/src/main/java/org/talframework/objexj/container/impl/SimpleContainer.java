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
package org.talframework.objexj.container.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.Event;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.QueryRequest;
import org.talframework.objexj.QueryResult;
import org.talframework.objexj.ValidationRequest;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerStrategy;
import org.talframework.objexj.container.InternalContainer;
import org.talframework.objexj.container.ObjectStrategy;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.exceptions.ContainerInvalidException;
import org.talframework.objexj.exceptions.EventHandlerNotFoundException;
import org.talframework.objexj.exceptions.ObjectNotFoundException;
import org.talframework.objexj.exceptions.ObjectTypeNotFoundException;
import org.talframework.objexj.object.RootObjexObj;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;
import org.talframework.objexj.object.writer.MapObjectReader;
import org.talframework.objexj.query.Query;

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

    /** The cache of all objects served by the container */
    private ContainerObjectCache cache;
    
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
        if( strategy == null ) throw new IllegalArgumentException("The container strategy must be present");
        if( middleware == null ) throw new IllegalArgumentException("The container middleware must be present");
        
        this.id = id;
        this.strategy = strategy;
        this.middleware = middleware;
        this.cache = middleware.init(this);
        
        // This is a new container, so create the root object
        if( this.id == null ) {
            if( !open ) throw new IllegalArgumentException("The container cannot be new (no ID) and not be open at creation!");
            
            ObjectStrategy rootStrategy = this.strategy.getObjectStrategy(this.strategy.getRootObjectName());
            if( rootStrategy == null ) throw new IllegalArgumentException("The container strategy has no root object or not strategy for the root object");
            
            // Get the root ID or form a new one
            ObjexID rootId = this.strategy.getRootObjectID();
            if( rootId == null ) rootId = new DefaultObjexID(rootStrategy.getTypeName(), 1);
            
            ObjexObj ret = rootStrategy.createInstance(this, null, rootId, null);
            MapObjectReader reader = new MapObjectReader(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
            reader.readObject(new HashMap<String, Object>(), ret);
            cache.addObject(ret, true);
        }
    }
    
    /**
     * Helper to quickly check the container is usable. The container
     * is not usable after saving, suspending or closing the container.
     * Also serves to ensure the container is setup correctly.
     */
    private void checkUsable() {
        if( cache == null ) throw new IllegalStateException("The container instance is not usable anymore, please re-open");
    }
    
    /**
     * Helper to determine if we are in a transaction or not.
     */
    private void checkInTransaction() {
        if( !middleware.isOpen() ) throw new IllegalStateException("The container [" + this.id + "] is not open so changes cannot be made");
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
     * Attempts to convert object into an ID
     */
    @Override
    public ObjexID getIdOfObject(Object obj) {
        ObjexID ret = null;
        
        if( obj instanceof ObjexID ) ret = (ObjexID)obj;
        else if( obj instanceof ObjexObj ) ret = ((ObjexObj)obj).getId();
        else if( obj instanceof String ) ret = DefaultObjexID.getId(obj);
        else {
            ObjectStrategy objectStrategy = strategy.getObjectStrategyForObject(obj);
            if( objectStrategy != null ) ret = objectStrategy.getObjexID(obj);
        }
        
        return ret;
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
        checkUsable();
        if( id == null ) return null; // Just protect from a stupid call!!
        
        ObjexID realId = DefaultObjexID.getId(id);
        if( realId == null ) throw new IllegalArgumentException("ID passed in does not appear to be valid: " + id);
        
        // See if we already have it
        ObjexObj ret = cache.getObject(realId, false);
        
        // Go to middleware if neccessary
        if( ret == null && middleware.exists(realId, false) ) {
            ObjectStrategy objectStrategy = strategy.getObjectStrategy(realId.getType());
            ret = objectStrategy.createInstance(this, null, realId, null);
            
            ret = middleware.loadObject(ret);
            if( ret != null ) cache.addObject(ret, false);
        }
        
        if( ret == null ) throw new ObjectNotFoundException(this.id, realId.toString()); 
        
        return ret;
    }
    
    /**
     * Simply gets each object on the list
     * 
     * FUTURE: Work out objects not held locally and get in one op
     */
    public List<ObjexObj> getObjectList(List<? extends Object> ids) {
        checkUsable();
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
    public Map<String, ObjexObj> getObjectMap(Map<String, ? extends Object> ids) {
        checkUsable();
        if( ids == null ) return null;
        
        Map<String, ObjexObj> ret = new HashMap<String, ObjexObj>();
        Iterator<String> it = ids.keySet().iterator();
        while( it.hasNext() ) {
            String k = it.next();
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
    public <T> Map<String, T> getObjectMap(Map<String, ? extends Object> ids, Class<T> expectedElement) {
        Map<String, ObjexObj> objs = getObjectMap(ids);
        Map<String, T> ret = objs != null ? new HashMap<String, T>(objs.size()) : null;
        
        if( objs != null ) {
            Iterator<String> it = objs.keySet().iterator();
            while( it.hasNext() ) {
                String key = it.next();
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
        checkUsable();
        Query query = strategy.getQuery(request.getName());
        return query.execute(this, strategy, request);
    }
    
    /**
     * Simply delegates to the event processor by name.
     * 
     * @throws EventHandlerNotFoundException If the event is unknown
     */
    public void processEvent(Event event) {
        checkUsable();
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
        if( isOpen() ) throw new IllegalArgumentException("The container is already open");
        middleware.open();
        if( !middleware.isOpen() ) throw new IllegalArgumentException("The container [" + this.id + "] does not allowing being opened after creation");
        return this;
    }
    
    /**
     * Asks the middleware if the container is new
     */
    public boolean isNew() {
        checkUsable();
        return middleware.isNew();
    }
    
    /**
     * Determines based on the transaction cache presence
     */
    public boolean isOpen() {
        checkUsable();
        return middleware.isOpen();
    }

    /**
     * Validates all new/changed objects inside container
     */
    public ValidationRequest validate() {
        ObjexObj obj = getRootObject();
        RootObjexObj root = (obj instanceof RootObjexObj) ? (RootObjexObj)obj : null; 
        
        // Validate
        ValidationRequest request = ContainerValidator.validate(cache, root != null ? root.getErrors() : null);
        
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
     * then saves via the middleware. After use the container
     * should no longer be used. 
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
        
        // b. Save
        String status = root != null ? root.getStatus() : null;
        Map<String, String> header = root != null ? root.getHeader() : null;
        id = middleware.save(cache, status, header);
        cache = null;
        return getId();
    }
    
    /**
     * Removes any caches and suspends the middleware. After use
     * the container should no longer be used.
     */
    public String suspend() {
        checkInTransaction();
        String ret = middleware.suspend(cache);
        cache = null;
        return ret;
    }
    
    /**
     * Removes any caches and clears the middleware.
     */
    public void closeContainer() {
        middleware.clear(cache);
        cache = null;
    }
    
    ///////////////////////////////////////////////
    // Internal Specific
    
    @Override
    public ObjexObj createObject(ObjexObj parentObj, Object source) {
        checkInTransaction();
        
        ObjectStrategy objectStrategy = strategy.getObjectStrategyForObject(source);
        if( objectStrategy == null ) throw new ObjectTypeNotFoundException(source != null ? source.toString() : "<>");
        
        // a. Create the new ID
        ObjexID id = objectStrategy.getObjexID(source);
        if( id == null ) id = middleware.getNextObjectId(objectStrategy.getTypeName());
        ObjexID parentId = parentObj != null ? parentObj.getId() : null;
        
        // b. Create the object & add to cache
        ObjexObj ret = objectStrategy.createInstance(this, parentId, id, source);
        cache.addObject(ret, true);
        
        return ret;
    }
    
    public void removeObject(ObjexObj obj) {
        checkInTransaction();
        
        cache.removeObject(obj);
    }
}
