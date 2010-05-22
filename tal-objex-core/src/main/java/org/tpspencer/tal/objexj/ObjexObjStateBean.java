package org.tpspencer.tal.objexj;

import java.io.Serializable;

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
     * @return The ObjexObj type that this bean represents
     */
    public String getType();

	/**
	 * @return The ID of the object this is the state bean for
	 */
	public Object getId();
	
	/**
	 * Call to set the objects ID. This can only be done
	 * if the ID is currently null (so assumed to be part
	 * of loading process) or if the ID is a 'new' ID. The
	 * later requires support in the beans which may not
	 * be present so ideally the ID of the object is
	 * constructed at creation time.
	 * 
	 * @param id The id of the parent object
	 */
	public void setId(Object id);
	
	/**
	 * @return The ID of the parent object to this one (if any) 
	 */
	public Object getParentId();
	
	/**
	 * Call to set the parent objects ID.
	 * 
	 * @param parentId The ID of the parent object
	 */
	public void setParentId(Object parentId);
}
