package org.tpspencer.tal.objexj.container;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.ObjexID;

/**
 * This interface represents the connection between a
 * container and its runtime environment. By abstracting
 * certain container elements into this interface we can
 * leverage the same container code in a number of
 * runtime environments. This interfaces responsibility
 * is to load objects and to control what an ObjexID is
 * and how to convert between it an other objects.
 * 
 * TODO: Method to load a bunch of objects!!
 * 
 * @author Tom Spencer
 */
public interface ContainerMiddleware {
	
	/**
	 * Call to initialise the middleware against the container.
	 * Each middleware instance is matched to a container
	 * instance.
	 * 
	 * @param container The container
	 */
	public void init(Container container);
	
	/**
	 * Call to get the containers ID. This is done because a
	 * middleware is often created based on a transaction ID.
	 * 
	 * @return The containers ID
	 */
	public String getContainerId();
	
	/**
	 * Called to actually load an object from the persistent
	 * store.
	 * 
	 * @param type The type of the object
	 * @param id The ID of the object
	 * @return The object
	 */
	public Object loadObject(Class<?> type, ObjexID id);
	
	// TODO: Load objects
	
}
