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

package org.talframework.objexj.container.middleware;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.talframework.objexj.Container;
import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexIDStrategy;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.ContainerMiddleware;
import org.talframework.objexj.container.DefaultObjexID;
import org.talframework.objexj.container.TransactionCache;
import org.talframework.objexj.container.TransactionCache.ObjectRole;
import org.talframework.objexj.container.impl.SimpleTransactionCache;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.exceptions.ObjectIDInvalidException;

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
    /** Holds the cache if we are open */
    private TransactionCache cache;
    /** Holds the ID strategy */
    private ObjexIDStrategy idStrategy;
    /** Holds the objects - if null its a new container */
    List<ObjexObjStateBean> objects = null; 
    
    /**
     * Constructs a new {@link InMemoryMiddleware} instance.
     * 
     * @param id The ID of the container or transaction
     * @param open Determines if the middleware should be open at the start
     */
    public InMemoryMiddleware(String id, boolean open) {
        if( id == null ) {
            this.id = null;
            this.cache = new SimpleTransactionCache();
            this.objects = null;
            this.idStrategy = new InMemoryIDStrategy();
            
        }
        else if( id.endsWith("/trans") ) {
            this.id = id.substring(0, id.length() - 6);
            this.cache = SingletonContainerCache.getInstance().getCache(id);
            this.objects = SingletonContainerStore.getInstance().getObjects(this.id);
            this.idStrategy = new InMemoryIDStrategy();
        }
        else {
            this.id = id;
            this.cache = open ? new SimpleTransactionCache() : null;
            this.objects = SingletonContainerStore.getInstance().getObjects(this.id);
            this.idStrategy = new InMemoryIDStrategy();
        }
    }

    public void init(Container container) {
        this.container = container;
    }
    
    public String getContainerId() {
        return id;
    }
    
    public ObjexIDStrategy getIdStrategy() {
        return idStrategy;
    }
    
    public TransactionCache getCache() {
        return cache;
    }
    
    public ObjexObjStateBean loadObject(Class<? extends ObjexObjStateBean> type, ObjexID id) {
        ObjexObjStateBean ret = null;
        
        // a. Try the cache first
        if( cache != null ) {
            ret = cache.findObject(id, null);
        }
        
        // b. Otherwise load if we are a real container
        if( ret == null ) {
            int realId = getObjectId(id);
            
            ObjexObjStateBean current = null;
            if( this.objects != null && realId >= 0 && realId < this.objects.size() ) {
                current = this.objects.get(realId);
            }
            
            if( current != null ) ret = current.cloneState();
        }
        
        return ret;
    }
    
    public boolean isNew() {
        return this.objects == null;
    }
    
    public TransactionCache open() {
        if( cache == null ) cache = new SimpleTransactionCache();
        return cache;
    }
    
    public String save(String status, Map<String, String> header) {
        if( cache == null ) throw new IllegalStateException("Middleware is not in a transaction, so cannot save");
        
        // Create container ID if it is null
        getOrGenerateContainerId();
        
        List<ObjexObjStateBean> existingObjs = SingletonContainerStore.getInstance().getObjects(id);
        Map<ObjexID, ObjexObjStateBean> newObjs = cache.getObjects(ObjectRole.NEW);
        Map<ObjexID, ObjexObjStateBean> updatedObjs = cache.getObjects(ObjectRole.UPDATED);
        
        // Create a new list for container
        int size = existingObjs != null ? existingObjs.size() : 0;
        size += newObjs != null ? newObjs.size() : 0;
        List<ObjexObjStateBean> objs = new ArrayList<ObjexObjStateBean>(size);
        for( int i = 0 ; i < size ; i++ ) objs.add(null);
        
        // Add on new objects
        Iterator<ObjexID> it = newObjs != null ? newObjs.keySet().iterator() : null;
        while( it != null && it.hasNext() ) {
            ObjexID id = it.next();
            int realId = getObjectId(id);
            if( realId < 0 || realId >= objs.size() ) throw new IllegalArgumentException("New object does not appear to have created via middleware ID strategy, in-memory middleware does not support custom IDs: " + id);
            objs.set(realId, newObjs.get(id));
        }
        
        // Add those that have changed
        it = updatedObjs != null ? updatedObjs.keySet().iterator() : null;
        while( it != null && it.hasNext() ) {
            ObjexID id = it.next();
            int realId = getObjectId(id);
            if( realId < 0 || realId >= objs.size() ) throw new IllegalArgumentException("New object does not appear to have created via middleware ID strategy, in-memory middleware does not support custom IDs: " + id);
            objs.set(realId, updatedObjs.get(id));
        }
        
        // Add all previous unless not null or removed
        if( existingObjs != null && existingObjs.size() > 0 ) {
            for( int i = 0 ; i < existingObjs.size() ; i++ ) {
                if( objs.get(i) != null ) continue;
                else if( cache.getObjectRole(new DefaultObjexID(existingObjs.get(i).getObjexObjType(), i)) == ObjectRole.NONE ) {
                    objs.set(i, existingObjs.get(i));
                }
            }
        }
        
        // Save
        SingletonContainerStore.getInstance().setObjects(id, objs);
        return id;
    }
    
    public String suspend() {
        String transId = getOrGenerateContainerId();
        transId += "/trans";
        
        SingletonContainerCache.getInstance().setCache(transId, cache);
        
        return transId;
    }
    
    public void clear() {
        cache = null;
    }
    
    /**
     * FUTURE: Support events via in-proc mechanism?
     */
    public void registerListener(EventListener listener) {
    }
    
    /**
     * FUTURE: Support events via in-proc mechanism?
     */
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
    
    /**
     * Internal helper to obtain the raw ID of the object.
     * 
     * @param id The full ID
     * @return Its internal index in the container
     */
    private int getObjectId(ObjexID id) {
        long realId = -1;
        if( id.getId() instanceof Long ) realId = (Long)id.getId();
        else realId = Long.parseLong(id.toString());
        realId -= 1;
        
        if( realId < 0 ) throw new ObjectIDInvalidException(id);
        return (int)realId;
    }
    
    /**
     * Simple ID generator that takes the size of the objects
     * in container as the start of any new ones.
     *
     * @author Tom Spencer
     */
    class InMemoryIDStrategy implements ObjexIDStrategy {
        
        int lastId = 1;
        
        public InMemoryIDStrategy() {
            lastId = objects != null ? objects.size() : 1;
            
            // Addjust for any in cache if we are a suspended transaction
            if( cache != null ) {
                Map<ObjexID, ObjexObjStateBean> newObjs = cache.getObjects(ObjectRole.NEW);
                if( newObjs != null ) lastId += newObjs.size();
            }
        }
        
        public ObjexID createId(Container container, Class<? extends ObjexObjStateBean> stateType, String type, ObjexObj obj) {
            return new DefaultObjexID(type, ++lastId);
        }
    }
}
