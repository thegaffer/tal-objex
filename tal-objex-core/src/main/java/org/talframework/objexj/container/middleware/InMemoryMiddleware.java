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
package org.talframework.objexj.container.middleware;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.Container;
import org.talframework.objexj.DefaultObjexID;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.ContainerObjectCache;
import org.talframework.objexj.container.ContainerObjectCache.CacheState;
import org.talframework.objexj.container.impl.DefaultContainerObjectCache;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.object.writer.BaseObjectReader.ObjectReaderBehaviour;
import org.talframework.objexj.object.writer.BaseObjectWriter.ObjectWriterBehaviour;
import org.talframework.objexj.object.writer.MapObjectReader;
import org.talframework.objexj.object.writer.MapObjectWriter;
import org.talframework.tal.aspects.annotations.Profile;
import org.talframework.tal.aspects.annotations.Trace;

/**
 * This class provides an implementation of the middleware that
 * operates purely in-memory. This has been created largely 
 * for integration test purposes.
 * 
 * FUTURE: Make this abstract so we can easily support other simple middlewares
 *
 * @author Tom Spencer
 */
public class InMemoryMiddleware implements ContainerMiddleware {
    
    /** Holds the ID of the container */
    private String id;
    /** Holds the container post-init */
    private Container container;
    /** Determines if we are open or not */
    private boolean open;
    
    /** Holds the objects - if null its a new container */
    private Map<ObjexID, Map<String, Object>> objects = null;
    /** Holds the transaction */
    private Map<ObjexID, Map<String, Object>> transaction = null;
    /** Holds the last ID of an object */
    private long lastId = 0;
    
    /**
     * Constructs a new {@link InMemoryMiddleware} instance.
     * 
     * @param id The ID of the container or transaction
     * @param open Determines if the middleware should be open at the start
     */
    public InMemoryMiddleware(String id, boolean open) {
        this.id = id;
        this.open = open;
        if( this.id != null && this.id.endsWith(":trans") ) this.id = id.substring(0, id.length() - 6);
        
        this.objects = this.id != null ? SingletonContainerStore.getInstance().getObjects(this.id) : null;
        
        // Determine highest ID if in transaction
        if( open ) {
            this.transaction = id != null ? SingletonContainerCache.getInstance().getCache(this.id) : null;
            
            if( this.objects != null ) {
                for( ObjexID objId : this.objects.keySet() ) updateLastId(objId);
            }
            if( this.transaction != null ) {
                for( ObjexID objId : this.transaction.keySet() ) updateLastId(objId);
            }
            else {
                this.transaction = new HashMap<ObjexID, Map<String,Object>>();
            }
        }
    }
    
    /**
     * Helper to update the last ID if the ID is a numeric ID.
     * Called from constructor
     * 
     * @param id The ID to test
     */
    private void updateLastId(ObjexID id) {
        try {
            long val = lastId;
            if( id.getId() instanceof Long ) lastId = (Long)id.getId();
            else val = Long.parseLong(id.getId().toString());
            
            if( val > lastId ) lastId = val;
        }
        catch( NumberFormatException e ) {}
    }

    /**
     * {@inheritDoc}
     * 
     * FUTURE: This is wrong, we should return full set of ObjexObjects in the cache
     */
    @Override
    public ContainerObjectCache init(Container container) {
        this.container = container;
        return new DefaultContainerObjectCache(1);
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
    @Trace
    public boolean exists(ObjexID id, boolean accurate) {
        return objects != null ? objects.containsKey(id) : false;
    }
    
    /**
     * {@inheritDoc}
     * 
     * FUTURE: This is wrong, the transaction objects should already be in the cache
     */
    @Override
    @Trace
    @Profile
    public ObjexObj loadObject(ObjexObj obj) {
        Map<String, Object> source = null;
        if( this.transaction != null ) source = this.transaction.get(obj.getId());
        if( source == null && this.objects != null ) source = this.objects.get(obj.getId());
        
        if( source == null ) return null;
        
        MapObjectReader reader = new MapObjectReader(ObjectReaderBehaviour.INCLUDE_OWNED, ObjectReaderBehaviour.INCLUDE_REFERENCES);
        reader.readObject(source, obj);
        return obj;
    }
    
    /**
     * {@inheritDoc}
     * 
     * In this container simply iterates around and loads them one by one.
     * If using this class as a basis for your own container I suggest thinking
     * whether you can batch load more efficiently here.
     */
    @Override
    @Trace
    @Profile
    public Map<ObjexID, ObjexObj> loadObjects(ObjexObj... objs) {
        Map<ObjexID, ObjexObj> ret = new HashMap<ObjexID, ObjexObj>();
        for( ObjexObj obj : objs ) {
            obj = loadObject(obj);
            if( obj != null ) ret.put(obj.getId(), obj);
        }
        return ret;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNew() {
        return this.objects == null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return open;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Trace
    public void open() {
        if( open ) throw new IllegalStateException("Container is already open");
        if( SingletonContainerCache.getInstance().getCache(getOrGenerateContainerId()) != null ) throw new IllegalStateException("There is already another transaction on this container, cannot open");
        
        // Find the highest ID
        open = true;
        if( this.objects != null ) {
            for( ObjexID objId : this.objects.keySet() ) {
                try {
                    long val = Long.parseLong(objId.getId().toString()); 
                    if( val > lastId ) lastId = val;
                }
                catch( NumberFormatException e ) {}
            }
        }
        
        this.transaction = new HashMap<ObjexID, Map<String,Object>>();
    }
    
    /**
     * {@inheritDoc}
     * 
     * In this simple middleware we simply increment a last index used
     */
    @Override
    @Trace
    public ObjexID getNextObjectId(String type) {
        if( !open ) throw new IllegalStateException("Middleware is not open so cannot generate a new ID");
        
        ObjexID ret = null;
        synchronized( this ) {
            ++lastId;
            ret = new DefaultObjexID(type, lastId);
        }
        
        return ret;
    }
    
    /**
     * {@inheritDoc}
     * 
     * FUTURE: This is wrong, the transaction objects should be in cache. We should only consider passed in cache for objects!
     */
    @Override
    @Trace
    @Profile
    public String save(ContainerObjectCache cache, String status, Map<String, String> header) {
        if( !open || this.transaction == null ) throw new IllegalStateException("Container is not open, or not open correctly");
        
        Map<ObjexID, Map<String,Object>> toSave = this.transaction;
        
        MapObjectWriter writer = new MapObjectWriter(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
        Set<ObjexObj> changed = cache.getObjects(CacheState.ACTIVE);
        for( ObjexObj obj : changed ) {
            Map<String, Object> props = toSave.get(obj.getId());
            if( props == null ) props = new HashMap<String, Object>();
            writer.writeObject(props, null, obj);
            toSave.put(obj.getId(), props);
        }
        
        // Merge in those objects
        if( this.objects == null ) this.objects = new HashMap<ObjexID, Map<String,Object>>();
        for( ObjexID id : toSave.keySet() ) {
            // TODO: Watch out for removed!!
            this.objects.put(id, toSave.get(id));
        }
        
        // TODO: Remove removed objects
        
        String containerId = getOrGenerateContainerId();
        SingletonContainerCache.getInstance().setCache(containerId, null);
        SingletonContainerStore.getInstance().setObjects(containerId, this.objects);
        
        return containerId;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Trace
    @Profile
    public String suspend(ContainerObjectCache cache) {
        if( !open || this.transaction == null ) throw new IllegalStateException("Container is not open, or not open correctly");
        
        MapObjectWriter writer = new MapObjectWriter(ObjectWriterBehaviour.INCLUDE_OWNED, ObjectWriterBehaviour.INCLUDE_REFERENCES);
        Set<ObjexObj> changed = cache.getObjects(CacheState.ACTIVE);
        for( ObjexObj obj : changed ) {
            Map<String, Object> props = this.transaction.get(obj.getId());
            if( props == null ) props = new HashMap<String, Object>();
            writer.writeObject(props, null, obj);
            this.transaction.put(obj.getId(), props);
        }
        
        // TODO: Special map holding removed objects with id _Removed/_removed
        
        String containerId = getOrGenerateContainerId();
        SingletonContainerCache.getInstance().setCache(containerId, this.transaction);
        return containerId + ":trans";
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Trace
    @Profile
    public void clear(ContainerObjectCache cache) {
        if( !open || this.transaction == null ) throw new IllegalStateException("Container is not open, or not open correctly");
        
        SingletonContainerCache.getInstance().setCache(getOrGenerateContainerId(), null);
    }
    
    /**
     * {@inheritDoc}
     * 
     * FUTURE: Support events via in-proc mechanism?
     */
    @Override
    public void registerListener(EventListener listener) {
    }
    
    /**
     * {@inheritDoc}
     * 
     * FUTURE: Support events via in-proc mechanism?
     */
    @Override
    public void registerListenerForTransaction(EventListener listener) {
    }
    
    ///////////////////////////////////////////////////////
    // Internal Helpers
    
    /**
     * Gets or Generates a new container ID
     */
    private String getOrGenerateContainerId() {
        if( this.id == null ) this.id = container.getType() + "/" + System.currentTimeMillis();
        return this.id;
    }
}
