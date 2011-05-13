package org.talframework.objexj.container.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;
import org.talframework.objexj.container.ContainerObjectCache;

public class DefaultContainerObjectCache implements ContainerObjectCache {
    
    /** The version of the container when created */
    private final long containerVersion;
    /** All pre-existing objects in the cache */
    private final Map<ObjexID, CachedObject> objects;
    /** Holds all objects added to container */
    private final Map<ObjexID, ObjexObj> newObjects;
    /** Holds all objects removed from container */
    private final Map<ObjexID, ObjexObj> removedObjects;
    
    /**
     * Default constructor for constructing the cache in the first instance 
     */
    public DefaultContainerObjectCache(long containerVersion) {
        this.containerVersion = containerVersion;
        objects = new HashMap<ObjexID, DefaultContainerObjectCache.CachedObject>();
        newObjects = new HashMap<ObjexID, ObjexObj>();
        removedObjects = new HashMap<ObjexID, ObjexObj>();
    }
    
    /**
     * Constructor for use by a middleware to re-load the cache from a temporary
     * store after suspending the container.
     * 
     * @param objects The loaded and changed objects
     * @param newObjects The objects created
     * @param removedObjects The objects removed
     */
    public DefaultContainerObjectCache(
            long containerVersion, 
            Map<ObjexID, CachedObject> objects,
            Map<ObjexID, ObjexObj> newObjects,
            Map<ObjexID, ObjexObj> removedObjects) {
        this.containerVersion = containerVersion;
        this.objects = objects != null ? objects : new HashMap<ObjexID, DefaultContainerObjectCache.CachedObject>();
        this.newObjects = newObjects != null ? newObjects : new HashMap<ObjexID, ObjexObj>();
        this.removedObjects = removedObjects != null ? removedObjects : new HashMap<ObjexID, ObjexObj>(); 
    }
    
    @Override
    public long getContainerVersion() {
        return containerVersion;
    }
    
    /**
     * Mainly for the middleware to access the cached objects so
     * they can be temporarily saved.
     * 
     * @return The cached objects
     */
    public Map<ObjexID, CachedObject> getCachedObjects() {
        return objects;
    }
    
    /**
     * Mainly for the middleware to access the new objects so
     * they can be temporarily saved.
     * 
     * @return The new objects
     */
    public Map<ObjexID, ObjexObj> getNewObjects() {
        return newObjects;
    }
    
    /**
     * Mainly for the middleware to access the removed objects so
     * they can be temporarily saved.
     * 
     * @return The removed objects
     */
    public Map<ObjexID, ObjexObj> getRemovedObjects() {
        return removedObjects;
    }

    @Override
    public ObjexObj getObject(ObjexID id, boolean includeRemoved) {
        ObjexObj ret = null;
        
        if( objects.containsKey(id) ) ret = objects.get(id).getObject();
        else if( newObjects.containsKey(id) ) ret = newObjects.get(id);
        else if( includeRemoved && removedObjects.containsKey(id) ) ret = removedObjects.get(id);
        
        return ret;
    }
    
    @Override
    public CacheState getObjectCacheState(ObjexID id) {
        CacheState ret = CacheState.NONE;
        
        if( objects.containsKey(id) ) ret = objects.get(id).isChanged() ? CacheState.CHANGED : CacheState.ACTIVE;
        else if( newObjects.containsKey(id) ) ret = CacheState.NEW;
        else if( removedObjects.containsKey(id) ) ret = CacheState.REMOVED;
        
        return ret;
    }
    
    @Override
    public Set<ObjexObj> getObjects(CacheState state) {
        Set<ObjexObj> ret = new HashSet<ObjexObj>();
        
        switch(state) {
        case ACTIVE:
            for( ObjexID id : objects.keySet() ) ret.add(objects.get(id).getObject());
            for( ObjexID id : newObjects.keySet() ) ret.add(newObjects.get(id));
            break;
            
        case CHANGED:
            for( ObjexID id : objects.keySet() ) { 
                CachedObject cachedObject = objects.get(id);
                if( cachedObject.isChanged() ) ret.add(cachedObject.getObject());
            }
            break;
            
        case NEW:
            for( ObjexID id : newObjects.keySet() ) ret.add(newObjects.get(id));
            break;
            
        case NEWORCHANGED:
            for( ObjexID id : objects.keySet() ) { 
                CachedObject cachedObject = objects.get(id);
                if( cachedObject.isChanged() ) ret.add(cachedObject.getObject());
            }
            for( ObjexID id : newObjects.keySet() ) ret.add(newObjects.get(id));
            break;
            
        case REMOVED:
            for( ObjexID id : removedObjects.keySet() ) ret.add(removedObjects.get(id));
            break;
        }
        
        return ret;
    }
    
    @Override
    public void addObject(ObjexObj obj, boolean newObject) {
        if( newObject ) newObjects.put(obj.getId(), obj);
        else objects.put(obj.getId(), new CachedObject(obj));
    }
    
    @Override
    public void removeObject(ObjexObj obj) {
        // If was a new obj, just remove it
        if( newObjects.containsKey(obj.getId()) ) {
            newObjects.remove(obj.getId());
        }
        
        // Otherwise add it to removed objects, removing from normal objs if required
        else {
            if( objects.containsKey(obj.getId()) ) objects.remove(obj.getId());
            removedObjects.put(obj.getId(), obj);
        }
    }
    
    @Override
    public void clear() {
        newObjects.clear();
        removedObjects.clear();
        objects.clear();
    }
    
    /**
     * This internal class is used to hold the object with its
     * initial version. This can be used to determine if the 
     * object has changed.
     *
     * @author Tom Spencer
     */
    public static class CachedObject {
        private final ObjexObj obj;
        private final int initialCode;
        
        public CachedObject(ObjexObj obj) {
            this.obj = obj;
            this.initialCode = obj.hashCode();
        }
        
        /**
         * Constructor to use when restoring an object
         * from some temp storage so that we keep the
         * hashcode of the object when it was first added
         * to the cache.
         * 
         * @param obj The object
         * @param code The initial hashcode of the object
         */
        public CachedObject(ObjexObj obj, int code) {
            this.obj = obj;
            this.initialCode = code;
        }
        
        /**
         * @return The object
         */
        public ObjexObj getObject() {
            return obj;
        }
        
        /**
         * @return The initial hashcode when the object was first added
         */
        public int getInitialHash() {
            return initialCode;
        }
        
        /**
         * @return True if the object has changed since being added, false otherwise
         */
        public boolean isChanged() {
            return obj.hashCode() != initialCode;
        }
    }
}
