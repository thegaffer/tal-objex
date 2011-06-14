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

import org.talframework.objexj.ObjexID;

/**
 * This class acts as the source for the containers in an
 * in-memory middleware.
 *
 * @author Tom Spencer
 */
public class SingletonContainerStore {
    private static final SingletonContainerStore INSTANCE = new SingletonContainerStore();
    
    /** Holds each and every container */
    private Map<String, Map<ObjexID, Map<String, Object>>> containers;
    
    /** Hidden Constructor */
    private SingletonContainerStore() {}
    
    /**
     * @return The single instance of this object
     */
    public static SingletonContainerStore getInstance() { 
        return INSTANCE;
    }
    
    /**
     * Call to get a list of objects for the given container.
     * The container ID cannot be null.
     * 
     * @param container The ID of the container
     * @return The list of objects (or null)
     */
    public Map<ObjexID, Map<String, Object>> getObjects(String container) {
        if( container == null ) throw new IllegalArgumentException("Cannot get objects from the container store for a null container");
        
        return containers != null ? containers.get(container) : null;
    }
    
    /**
     * Call to set the objects against a given container for later
     * retrieval.
     * 
     * @param container The ID of the container (cannot be null)
     * @param objs The objects that constitute that container
     */
    public void setObjects(String container, Map<ObjexID, Map<String, Object>> objs) {
        if( container == null ) throw new IllegalArgumentException("Cannot set objects in the container store for a null container");
        
        if( containers == null ) containers = new HashMap<String, Map<ObjexID,Map<String,Object>>>();
        if( objs != null ) containers.put(container, objs);
        else containers.remove(container);
    }
}
