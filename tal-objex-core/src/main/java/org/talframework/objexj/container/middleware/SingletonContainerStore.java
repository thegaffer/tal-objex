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
