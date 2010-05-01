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
	 * Called to convert some object that represents the ID
	 * of an object into a real ObjexID. If the ID is null
	 * in then no error is thrown, but it is an implementation
	 * choice over a null return or an actual return that
	 * represents a Null ID.
	 * 
	 * @param id The id to convert
	 * @return The ObjexID
	 * @throws IllegalArgumentException If the ID is not a real ID.
	 */
	public ObjexID convertId(Object id);
	
	/**
	 * Called to determine the type from the ObjexID. This 
	 * used internally by the container and is no supported
	 * in all cases. However, typically the type of an 
	 * object is encoded into the type.
	 * 
	 * @param id The ID we want the type from
	 * @return The type
	 */
	public String getObjectType(ObjexID id);
	
	/**
	 * Called to actually load an object from the persistent
	 * store.
	 * 
	 * @param type The type of the object
	 * @param id The ID of the object
	 * @return The object
	 */
	public Object loadObject(Class<?> type, ObjexID id);
}
