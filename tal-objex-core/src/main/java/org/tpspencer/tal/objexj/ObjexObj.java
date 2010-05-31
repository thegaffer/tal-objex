package org.tpspencer.tal.objexj;

import java.util.Collection;
import java.util.Map;

/**
 * This interface represents an actual object held by
 * a container. 
 * 
 * @author Tom Spencer
 */
public interface ObjexObj {
    
    /**
	 * @return The ID of this object
	 */
	public ObjexID getId();
	
	/**
	 * @return The type name of this object
	 */
	public String getType();
	
	/**
	 * @return The ID of this objects parent
	 */
	public ObjexID getParentId();
	
	/**
	 * @return The objects parent
	 */
	public ObjexObj getParent();
	
	/**
	 * @return The root object in the container (amy or may not be parent)
	 */
	public ObjexObj getRoot();
	
	/**
	 * @return The container this object was served from
	 */
	public Container getContainer();
	
	/**
	 * This method returns the inner state (or properties) or the
	 * object. Unless we are in a transaction this will be a clone
	 * of the state object and any changes made to it will not be
	 * reflected on this object or elsewhere on the container.
	 * 
	 * @return The state object
	 */
	public Object getStateObject();
	
	/**
	 * Call to get a behaviour interface from this object. We 
	 * encourage the use of this method over instanceof on
	 * ObjexObj instances in case the interface is supported
	 * only at certain points in the lifecycle of the object
	 * or at times when the object contains strong-form
	 * compound elements.
	 * 
	 * @param behaviour The type of behaviour required
	 * @return
	 */
	public <T> T getBehaviour(Class<T> behaviour);
	
	/**
	 * Convienience version of getStateObject when you know the
	 * type of the object. As above this will be a cloned instance
	 * unless we are inside an {@link EditableContainer}.
	 * 
	 * @param expected The expected type of the state object
	 * @return The state object
	 */
	public <T> T getStateObject(Class<T> expected);
	
	/**
	 * Helper to get a property given its name. This is
	 * a convienience to save the client having to do
	 * reflection if it does not know the concrete type.
	 * 
	 * <p>Note: With this version of the method the property
	 * is returned as a string. The formatting is determined
	 * by the object. If you prefer to do you own formatting
	 * use the getProperty method below.
	 * 
	 * @param name The name of the property required
	 * @return The value of the property as a string
	 */
	public String getPropertyAsString(String name);
	
	/**
	 * Helper to get a property given its name. This is
	 * a convienience to save the client having to do
	 * reflection if it does not know the concrete type.
	 * 
	 * @param name The name of the property required
	 * @return The value of the property
	 */
	public Object getProperty(String name);
	
	/**
	 * Gets a component or reference property as a native
	 * object instead of the direct key.
	 * 
	 * <p><b>Note: </b>This only works inside the same container
	 *  
	 * @param name The name of the property holding the reference
	 * @return The object
	 */
	public ObjexObj getReference(String name);
	
	/**
	 * Gets a component or reference property as a native
	 * collection of objects instead of the direct keys.
	 * 
	 * <p><b>Note: </b>This only works inside the same container
	 * 
	 * @param name The name of the property holding the reference
	 * @return The collection of objects
	 */
	public Collection<ObjexObj> getCollectionReference(String name);
	
	/**
	 * Gets a component or reference property as a native
	 * collection of objects instead of the direct keys.
	 * 
	 * <p><b>Note: </b>This only works inside the same container
	 * 
	 * @param name The name of the property holding the reference
	 * @return The map of objects
	 */
	public Map<ObjexID, ObjexObj> getMapReference(String name);
}
