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
package org.talframework.objexj.container;

import java.util.Set;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObj;

/**
 * This interface represents an in-memory cache of objects. This
 * is held by the container whilst it exists and is passed back
 * to the middleware when the container is saved or suspended. If
 * the container is closed the cache is just discarded.
 * 
 * @author Tom Spencer
 */
public interface ContainerObjectCache {
    
    /**
     * The version of the container is tracked so it is easy for 
     * the middleware to determine if another user has edited the
     * document.
     * 
     * @return The version of the container when this cache was first started
     */
    public long getContainerVersion();

    /**
     * Call to get a specific object from the cache.
     * 
     * @param id The ID of the object to get
     * @param includeRemoved Only if this is true will a removed object be returned
     * @return The object requested
     */
    public ObjexObj getObject(ObjexID id, boolean includeRemoved);
    
    /**
     * Call to get the most specific state of the give object
     * in the cache. Please note where an object has dual states,
     * the most specific state is returned, i.e. is NEW (therefore 
     * also ACTIVE), NEW is returned.
     * 
     * @param id The ID of the object
     * @return The state of the object in this cache
     */
    public CacheState getObjectCacheState(ObjexID id);
    
    /**
     * Call to get the objects that are in the cache. Clearly
     * if cache state is none, an empty set is returned.
     * 
     * @param state The state of objects in the cache to obtain (see {@link CacheState})
     * @return The set of those objects held by cache
     */
    public Set<ObjexObj> getObjects(CacheState state);
    
    /**
     * Called by the container whenever it serves up a new
     * object or creates a new one inside a transaction.
     * 
     * @param obj The object
     * @param newObject Indicates if it is truly new to the container (true), or just loaded (false)
     */
    public void addObject(ObjexObj obj, boolean newObject);
    
    /**
     * Called when an object is removed from the container.
     * 
     * @param obj The object to remove
     */
    public void removeObject(ObjexObj obj);
    
    /**
     * This is called after a container is persisted to reset the
     * cache.
     */
    public void clear();
    
    /**
     * This enumeration contains the different states of
     * the objects in the cache.
     *
     * @author Tom Spencer
     */
    public enum CacheState {
        /** Object does not exist in the cache */
        NONE,
        /** All objects except removed ones */ 
        ACTIVE,
        /** All objects that have changed since being added */
        CHANGED,
        /** All objects that have been created */
        NEW,
        /** Special value for getting all new or changed objects together */
        NEWORCHANGED,
        /** All objects that have been removed */
        REMOVED
    }
}
