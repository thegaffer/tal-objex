package org.talframework.objexj.container;

import java.util.Map;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.ObjexObjStateBean;

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
     * @param role The role of the object (or null if not known)
     * @return The object in the cache
     */
    public ObjexObjStateBean findObject(ObjexID id, ObjectRole role);
    
    /**
     * Call to get the role this object is playing in the
     * transaction.
     * 
     * @param id The ID of the object
     * @return Its role
     */
    public ObjectRole getObjectRole(ObjexID id);

	/**
	 * Call to add an object to the transaction with the given
	 * role. It is not possible to downgrade an objects role, so
	 * if the object is already in the transaction as new or
	 * updated then an instruction to add it to the transaction 
	 * with the {@link ObjectRole#PARENT} role will be ignored.
	 * 
	 * @param role The role to add the object with
	 * @param id The ID of the object to add
	 * @param state The state of this object.
	 */
	public void addObject(ObjectRole role, ObjexID id, ObjexObjStateBean state);
	
	/**
	 * Gets map of all objects playing the given role inside the
	 * transaction.
	 * 
	 * @param role The role we want
	 * @return The map of object IDs to the state bean
	 */
	public Map<ObjexID, ObjexObjStateBean> getObjects(ObjectRole role);
	
	/**
     * Called after the container is saved to release all objects.
     */
    public void clear();
	
	/**
	 * This enum indicates the role of the object inside
	 * the transaction.
	 *
	 * @author Tom Spencer
	 */
	public static enum ObjectRole {
	    /** Indicates the object has no role in the transaction */
	    NONE,
	    /** Indicates the object is new in the transaction */
	    NEW,
	    /** Indicates the object is updated in the transaction */
	    UPDATED,
	    /** Indicates the object is removed in the transaction */
	    REMOVED,
	    /** Special role to the original state of any changed and removed objects */
	    AUDIT
	}
}
