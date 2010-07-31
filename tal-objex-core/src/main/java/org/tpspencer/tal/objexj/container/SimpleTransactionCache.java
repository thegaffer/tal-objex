package org.tpspencer.tal.objexj.container;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

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
	
	public void clear() {
		newObjects = null;
		updatedObjects = null;
		removedObjects = null;
	}
	
	public ObjexObjStateBean findObject(ObjexID id) {
		ObjexObjStateBean ret = null;
		if( isDeleted(id) ) {
		    ret = null;
		}
		else if( newObjects != null && newObjects.containsKey(id) ) {
			ret = newObjects.get(id);
		}
		else if( updatedObjects != null && updatedObjects.containsKey(id) ) {
			ret = updatedObjects.get(id);
		}
		
		return ret;
	}
	
	/**
	 * Simply determines if removed
	 */
	public boolean isDeleted(ObjexID id) {
	    if( removedObjects != null && removedObjects.containsKey(id) ) return true;
	    return false;
	}
	
	/**
	 * Simple adds the object to the set being added.
	 * 
	 * @param id The ID of the object
	 */
	public void addNewObject(ObjexID id, ObjexObjStateBean state) {
		if( newObjects == null ) newObjects = new HashMap<ObjexID, ObjexObjStateBean>();
		newObjects.put(id, state);
	}
	
	/**
	 * Simple adds the object to the set being updated
	 * 
	 * @param obj The existing object we are updating
	 */
	public void addUpdatedObject(ObjexID id, ObjexObjStateBean state) {
	    if( newObjects != null && newObjects.containsKey(id) ) return;
	    if( removedObjects != null && removedObjects.containsKey(id) ) return;
	    
		if( updatedObjects == null ) updatedObjects = new HashMap<ObjexID, ObjexObjStateBean>();
		updatedObjects.put(id, state);
	}
	
	/**
	 * Simple adds the object to the set being removed.
	 * 
	 * @param obj The object we are removing
	 */
	public void addRemovedObject(ObjexID id, ObjexObjStateBean state) {
	    if( newObjects != null && newObjects.containsKey(id) ) {
	        newObjects.remove(id);
	        return; // never existed
	    }
	    
	    if( updatedObjects != null && updatedObjects.containsKey(id) ) {
	        updatedObjects.remove(id);
	    }
	    
		if( removedObjects == null ) removedObjects = new HashMap<ObjexID, ObjexObjStateBean>();
		removedObjects.put(id, state);
	}
	
	public Map<ObjexID, ObjexObjStateBean> getNewObjects() {
		return newObjects;
	}
	
	public Map<ObjexID, ObjexObjStateBean> getRemovedObjects() {
		return removedObjects;
	}
	
	public Map<ObjexID, ObjexObjStateBean> getUpdatedObjects() {
		return updatedObjects;
	}
}
