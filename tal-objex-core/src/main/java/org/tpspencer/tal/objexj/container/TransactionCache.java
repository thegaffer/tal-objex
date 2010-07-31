package org.tpspencer.tal.objexj.container;

import java.util.Map;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObjStateBean;

/**
 * This interface represents a cache of objects that are 
 * part of a transaction.
 * 
 * @author Tom Spencer
 */
public interface TransactionCache {
    
    /**
     * Find the object in the cache.
     * 
     * @param id The ID of the object
     * @return The object in the cache
     */
    public ObjexObjStateBean findObject(ObjexID id);
    
    /**
     * Determine if an object is pending deletion.
     * 
     * @param id The ID of the object
     * @return True if it is.
     */
    public boolean isDeleted(ObjexID id);

	/**
	 * Simple adds the object to the set being added.
	 * 
	 * @param obj The object we are adding
	 */
	public void addNewObject(ObjexID id, ObjexObjStateBean state);
	
	/**
	 * Simple adds the object to the set being updated
	 * 
	 * @param obj The existing object we are updating
	 */
	public void addUpdatedObject(ObjexID id, ObjexObjStateBean state);
	
	/**
	 * Simple adds the object to the set being removed.
	 * 
	 * @param obj The object we are removing
	 */
	public void addRemovedObject(ObjexID id, ObjexObjStateBean state);
	
	/**
	 * @return The map of new objects being added
	 */
	public Map<ObjexID, ObjexObjStateBean> getNewObjects();
	
	/**
	 * @return The map of objects that have been updated
	 */
	public Map<ObjexID, ObjexObjStateBean> getRemovedObjects();
	
	/**
	 * @return The map of objects being deleted
	 */
	public Map<ObjexID, ObjexObjStateBean> getUpdatedObjects();
	
	/**
	 * Called after the container is saved to release all objects.
	 */
	public void clear();
}
