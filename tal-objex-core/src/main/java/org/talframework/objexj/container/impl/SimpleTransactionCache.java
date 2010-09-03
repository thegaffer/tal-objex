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

package org.talframework.objexj.container.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;
import org.talframework.objexj.container.TransactionCache;

/**
 * Simple implementation of the TransactionCache which just
 * holds the new, updated and removed objects. As this 
 * class is serialisable it is of use in multiple situations.
 * 
 * @author Tom Spencer
 */
public final class SimpleTransactionCache implements TransactionCache, Serializable {
	private static final long serialVersionUID = 1L;

	/** Holds any objects added in this transaction */
	private Map<ObjexID, ObjexObjStateBean> newObjects;
	/** Holds any objects updated in this transaction */
	private Map<ObjexID, ObjexObjStateBean> updatedObjects;
	/** Holds any objects removed in this transaction */
	private Map<ObjexID, ObjexObjStateBean> removedObjects;
	
	/** Holds the original state of each object */
	private Map<ObjexID, ObjexObjStateBean> auditStates;
	
	/**
	 * Gets the role if not supplied and then returns the object
	 */
	public ObjexObjStateBean findObject(ObjexID id, ObjectRole role) {
	    if( role == null ) role = getObjectRole(id);
	    
	    ObjexObjStateBean ret = null;
	    switch(role) {
	    case NEW:
	        ret = newObjects.get(id);
	        break;
	        
	    case UPDATED:
	        ret = updatedObjects.get(id);
	        break;
	        
	    case REMOVED:
	        ret = removedObjects.get(id);
	        break;
	        
	    case AUDIT:
	        ret = auditStates.get(id);
	        break;
	    }
	    
	    return ret;
    }
	
	/**
	 * Determines the role based on the member it is held within
	 */
	public ObjectRole getObjectRole(ObjexID id) {
	    ObjectRole ret = ObjectRole.NONE;
	    
	    if( newObjects != null && newObjects.containsKey(id) ) ret = ObjectRole.NEW;
	    if( updatedObjects != null && updatedObjects.containsKey(id) ) ret = ObjectRole.UPDATED;
	    if( removedObjects != null && removedObjects.containsKey(id) ) ret = ObjectRole.REMOVED;
	    
	    return ret;
	}
	
	/**
	 * Adds the object to appropriate member based on role. If adding
	 * a new or updated object it is removed from parent map. If
	 * removing an object is it removed from any map it currently 
	 * exists on.
	 */
	public void addObject(ObjectRole role, ObjexID id, ObjexObjStateBean state) {
	    ObjectRole existingRole = getObjectRole(id);
	    
	    switch(role) {
	    case NEW:
	        if( existingRole != ObjectRole.NONE ) break;
	        if( newObjects == null ) newObjects = new HashMap<ObjexID, ObjexObjStateBean>();
	        newObjects.put(id, state);
	        break;
	        
	    case UPDATED:
	        if( existingRole != ObjectRole.NONE ) break;
            if( updatedObjects == null ) updatedObjects = new HashMap<ObjexID, ObjexObjStateBean>();
            updatedObjects.put(id, state);
            
            if( auditStates == null ) auditStates = new HashMap<ObjexID, ObjexObjStateBean>();
            auditStates.put(id, state.cloneState());

            break;
            
	    case REMOVED:
	        if( existingRole == ObjectRole.NEW ) newObjects.remove(id);
	        else if( existingRole == ObjectRole.UPDATED ) updatedObjects.remove(id);
            else if( existingRole != ObjectRole.NONE ) break;
            if( removedObjects == null ) removedObjects = new HashMap<ObjexID, ObjexObjStateBean>();
            removedObjects.put(id, state);
            break;
	    }
	}
	
	/**
	 * Simply returns the map appropriate to role
	 */
	public Map<ObjexID, ObjexObjStateBean> getObjects(ObjectRole role) {
	    Map<ObjexID, ObjexObjStateBean> ret = null;
	    
	    switch(role) {
	    case NEW:
	        ret = newObjects;
	        break;
	        
	    case UPDATED:
	        ret = updatedObjects;
            break;
	        
	    case REMOVED:
	        ret = removedObjects;
            break;
            
	    case AUDIT:
	        ret = auditStates;
	        break;
	    }
	    
	    return ret;
	}
	
	public void clear() {
		newObjects = null;
		updatedObjects = null;
		removedObjects = null;
		auditStates = null;
	}
}
