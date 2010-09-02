package org.tpspencer.tal.objexj.object;

import java.util.Map;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexIDStrategy;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.container.InternalContainer;

/**
 * This interface represents something that can describe
 * the strategy for a particular object inside a container.
 * The strategy includes the type of object that holds the
 * state of the object and can create ObjexObj instances
 * as appropriate.
 * 
 * TODO: Remove the ID/ParentID Prop Name
 * 
 * @author Tom Spencer
 */
public interface ObjectStrategy {

	/**
	 * @return The short name for this object type
	 */
	public String getTypeName();
	
	/**
	 * Some objects have a custom ID strategy instead of a 
	 * middleware assigned strategy. These objects should
	 * have an ID strategy and return a temp ID when it is
	 * first created. The ID strategy will then be re-used 
	 * when the object is saved.
	 * 
	 * @return The ID strategy for the object
	 */
	public ObjexIDStrategy getIdStrategy();
	
	/**
	 * Call to create an ObjexObj instance for this object.
	 * 
	 * @param container The container initialising this object
	 * @param parent The ID of the parent of this object (if there is one)
	 * @param id The ID of this object
	 * @param state The state of the object
	 * @return The objex obj instance
	 */
	public ObjexObj getObjexObjInstance(InternalContainer container, ObjexID parent, ObjexID id, ObjexObjStateBean state);
	
	/**
	 * Call to get the class that represents the objects
	 * state. This should be at least serialisable and
	 * very likely describes the persistence via annotations
	 * for use with many types of container.
	 * 
	 * @return The objects state class
	 */
	public Class<? extends ObjexObjStateBean> getStateClass();
	
	/**
	 * Called to get the reference properties on this object.
	 * This is currently only needed if using the {@link SimpleObjexObj}
	 * class.
	 * 
	 * <p>The returned map holds the name of the reference
	 * property together with it's preferred method to hold
	 * the IDs as. This can either be {@link ObjexID}, 
	 * {@link String} or simple {@link Object} (which will 
	 * be treated as {@link ObjexID} when setting.</p>
	 * 
	 * @return The reference properties and the class they represent IDs as
	 */
	public Map<String, Class<?>> getReferenceProperties();
	
	/**
	 * Called to get the super set of reference properties that
	 * are 'owned' by this object - that is 'this' object owns
	 * the lifecycle of the owned object.
	 * 
	 * @return The owned reference properties and the class they represents IDs as
	 */
	public Map<String, Class<?>> getOwnedReferenceProperties();
}