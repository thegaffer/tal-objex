package org.tpspencer.tal.objexj.object;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;

/**
 * This interface represents something that can describe
 * the strategy for a particular object inside a container.
 * The strategy includes the type of object that holds the
 * state of the object and can create ObjexObj instances
 * as appropriate.
 * 
 * @author Tom Spencer
 */
public interface ObjectStrategy {

	/**
	 * @return The short name for this object type
	 */
	public String getTypeName();
	
	/**
	 * Call to create an ObjexObj instance for this object.
	 * 
	 * @param container The container initialising this object
	 * @param parent The ID of the parent of this object (if there is one)
	 * @param id The ID of this object
	 * @param state The state of the object
	 * @return The objex obj instance
	 */
	public ObjexObj getObjexObjInstance(Container container, ObjexID parent, ObjexID id, Object state);
	
	/**
	 * Call to get the class that represents the objects
	 * state. This should be at least serialisable and
	 * very likely describes the persistence via annotations
	 * for use with many types of container.
	 * 
	 * @return The objects state class
	 */
	public Class<?> getStateClass();
	
	/**
	 * Call for the strategy to create a new instance of the
	 * state class. This method may not return an instance
	 * of the state class returned above. The main motiviation
	 * for this method is to clone the persisted state instance.
	 * 
	 * @return A new instance of the state class
	 */
	public Object getNewStateInstance();
	
	/**
	 * @return The name of the property on state object holding the ID
	 */
	public String getIdProp();
	
	/**
	 * @return The name of the property on state object holding parent ID
	 */
	public String getParentIdProp();
}