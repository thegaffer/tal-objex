package org.tpspencer.tal.objexj;

import java.util.Collection;
import java.util.Map;

/**
 * This interface represents wrapper (or container) or related
 * objects. The container might be a store, which is akin to a
 * rolodex or similar, or a document which is akin to a folder
 * in a filing cabinet. Inside the folder or the rolodex are 
 * pieces of paper - these are the objects.
 * 
 * <p>The container interface provides read-only facilities to
 * query the container and get at its objects. The 
 * {@link EditableContainer} interface represents a container
 * that can be edited.</p>
 * 
 * @author Tom Spencer
 */
public interface Container {

	/**
	 * @return The ID of the container
	 */
	public String getId();
	
	/**
	 * @return The type of the container
	 */
	public String getType();
	
	/**
	 * @return The root object for the container
	 */
	public ObjexObj getRootObject();
	
	/**
	 * Get an object given its ID
	 * 
	 * @param id The ID of the object (may be an ObjexID, may be a string)
	 * @param expected The expected type of that object
	 * @return The object (or null if not found)
	 * @throws IllegalArgumentException If a null object id is supplied
	 */
	public <T> T getObject(Object id, Class<T> expected);
	
	/**
	 * Call to get an object given its ID
	 * 
	 * @param id The ID of the object (may be an ObjexID, may be a string)
	 * @return The object (or null if not found)
	 * @throws IllegalArgumentException If a null object id is supplied
	 */
	public ObjexObj getObject(Object id);
	
	/**
     * Call to get the real ID of an object given the
     * generic ID passed in (which may be the ID or a
     * string ref). Getting a non-null return does not
     * ensure the ID is a real object in the container,
     * but it does ensure its a valid ID.
     * 
     * @param id The ID of the object (may be an ObjexID, may be a string)
     * @return The ID of the object
     * @throws IllegalArgumentException If a null object id is supplied
     */
    public ObjexID getObjectId(Object id);
	
	/**
	 * Call to get a collection of objects given an array of IDs.
	 * This function is primarily for internal use.
	 *  
	 * @param ids The IDs in to get
	 * @return A collection of the valid objects found
	 */
	public Collection<ObjexObj> getObjects(ObjexID[] ids);
	
	/**
	 * Call to get a map, keyed by the objects ID given an array
	 * if IDs. This function is primarily for internal use.
	 * 
	 * @param ids The IDs in to get
	 * @return A collection of the valid objects found
	 */
	public Map<ObjexID, ObjexObj> getObjectMap(ObjexID[] ids);
	
	/**
	 * Call to open this container for editing. The returned 
	 * instance will typically be of a different object that
	 * you should now interact with instead of this instance.
	 * 
	 * @return An editable container to start working with 
	 * @deprecated
	 */
	public EditableContainer openContainer();
}
