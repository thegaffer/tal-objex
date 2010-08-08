package org.tpspencer.tal.objexj;

import java.io.Serializable;
import java.util.Map;

/**
 * This interface is a representative interface marking
 * a class that acts as the basic persistable state bean
 * of an ObjexObj. It is not part of the Objex API to
 * use this interface, but many middlewares require it. 
 * 
 * <p>Note: I've thought long and hard about this 
 * interface because it would mean marking the core
 * beans with something from Objex. However, I've 
 * resolved that it or something like this is pretty
 * much required, so I've put this interface in as a
 * start for 10. It is entirely possible for a 
 * middleware to use reflection and convention instead
 * of this interface.</p>
 * 
 * @author Tom Spencer
 */
public interface ObjexObjStateBean extends Serializable {
    
    /**
     * Called in the transaction before save to set the
     * runtime ID of the object
     * 
     * @param id The underlying runtime environment ID
     */
    public void init(Object id);
    
    /**
     * Called to update any temporary references this
     * object may be holding. This is done as the transaction
     * commits to turn any temp IDs into real IDs.
     * 
     * @param refs A map holding the original temp ID and its new real ID
     */
    public void updateTemporaryReferences(Map<ObjexID, ObjexID> refs);
    
    /**
     * @return The ObjexObj type that this bean represents
     */
    public String getObjexObjType();
    
    /**
     * @return The raw ID of the object (will likely not be an ObjexID)
     */
    public Object getId();

	/**
	 * @return The ID of the parent object to this one (if any) 
	 */
	public String getParentId();
	
}
