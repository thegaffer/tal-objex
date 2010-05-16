package org.tpspencer.tal.objexj.object;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;

/**
 * Base class for an ObjexObj. Most basic methods are implemented
 * but all can be overridden as required.
 * 
 * @author Tom Spencer
 */
public class SimpleObjexObj implements ObjexObj {

	/** The strategy for this object */
	private final ObjectStrategy strategy;
	/** The container that the object belongs to */
	private final Container container;
	/** Determines if object is in transaction already */
	private boolean inTransaction;
	
	/** ID of the object */
	private ObjexID id;
	/** ID of this object parent (or null if this is the root object) */
	private ObjexID parentId;
	/** Cached parent object */
	private ObjexObj parent;
	
	/** Member holds the detail or state object */
	private Object state;
	
	public SimpleObjexObj(ObjectStrategy strategy, Container container, ObjexID id, ObjexID parent, Object state) {
		if( strategy == null ) throw new IllegalArgumentException("Cannot create object without a strategy");
		if( container == null ) throw new IllegalArgumentException("Cannot create object without a container");
		if( id == null || id.isNull() ) throw new IllegalArgumentException("Cannot create object without a valid id");
		if( state == null ) throw new IllegalArgumentException("Cannot create object without a state object");
		
		this.strategy = strategy;
		this.container = container;
		this.id = id;
		this.parentId = parent;
		this.state = state;
	}
	
	/**
	 * Simply returns the ID
	 */
	public ObjexID getId() {
		return id;
	}
	
	/**
	 * Default is simple name of outer class
	 */
	public String getType() {
		return this.getClass().getSimpleName();
	}
	
	/**
	 * Simply returns the parent ID
	 */
	public ObjexID getParentId() {
		return parentId;
	}
	
	/**
	 * Gets the parent from the container and caches
	 * it so it does not do this again.
	 */
	public ObjexObj getParent() {
		if( parentId == null || parentId.isNull() ) return null;
		
		if( parent == null ) {
			synchronized (this) {
				parent = container.getObject(parentId);
			}
		}
		
		return parent;
	}
	
	/**
	 * Either returns self if we have no parent or asks
	 * parent for the root
	 */
	public ObjexObj getRoot() {
		if( parentId == null || parentId.isNull() ) return this;
		else return getParent().getRoot();
	}
	
	/**
	 * Simply returns the container
	 */
	public Container getContainer() {
		return container;
	}
	
	/**
	 * Returns either the state object directly or a copy
	 * depending on if in open editable container.
	 */
	public Object getStateObject() {
		if( container instanceof EditableContainer &&
				((EditableContainer)container).isOpen() )
			return state;
		else
			return cloneState();
	}
	
	/**
	 * The default method tests whether the instance supports
	 * this interface.
	 */
	public <T> T getBehaviour(Class<T> behaviour) {
		if( behaviour.isInstance(this) ) return behaviour.cast(this);
		else throw new ClassCastException("The behaviour is not supported by this object");
	}
	
	/**
	 * Simply checks if we are in an Editable Container
	 */
	public boolean isUpdatableable() {
	    if( inTransaction ) return true;
	    else if( container instanceof EditableContainer &&
				((EditableContainer)container).isOpen() )
			return true;
		else 
			return false;
	}
	
	/**
	 * 
	 */
	public void checkUpdateable(boolean placeInTransaction) {
		if( !isUpdatableable() ) throw new IllegalArgumentException("Cannot update an object that is not inside a transaction: " + this);
		if( placeInTransaction && !inTransaction ) {
		    ((EditableContainer)container).updateObject(this.id, this);
		    inTransaction = true;
		}
	}
	
	/**
	 * Helper that returns the state object without 
	 * cloning it. Can be used by the derived class.
	 * 
	 * @return The state object
	 */
	protected <T> T getLocalState(Class<T> expected) {
		return expected.cast(state);
	}
	
	/**
	 * Returns either the state object directly or a copy
	 * depending on if in open editable container.
	 */
	public <T> T getStateObject(Class<T> expected) {
		return expected.cast(getStateObject());
	}
	
	/**
	 * Uses the name as a property and reflects value
	 * turning it into a String if non-null
	 */
	public String getPropertyAsString(String name) {
		BeanWrapper wrapper = new BeanWrapperImpl(state);
		Object obj = wrapper.getPropertyValue(name);
		return obj != null ? obj.toString() : null;
	}
	
	/**
	 * Uses the name as a property and reflects value
	 */
	public Object getProperty(String name) {
		BeanWrapper wrapper = new BeanWrapperImpl(state);
		return wrapper.getPropertyValue(name);
	}
	
	/**
	 * Returns the reference in the given property as
	 * an object using the container to resolve.
	 */
	public ObjexObj getReference(String name) {
		Object id = getProperty(name);
		return id != null ? container.getObject(id) : null;
	}
	
	/**
	 * Gets the collection of objects from the container having
	 * got IDs from reference property. The IDs can be an array
	 * of ObjexID, an array of Strings or a collection of either.
	 */
	public Collection<ObjexObj> getCollectionReference(String name) {
		ObjexID[] ids = getReferenceIds(name);
		return ids != null ? container.getObjects(ids) : null;
	}
	
	/**
	 * Gets the IDs and asks the container to fill map
	 * 
	 * TODO: IDs should be held as a map by object!
	 */
	public Map<ObjexID, ObjexObj> getMapReference(String name) {
		ObjexID[] ids = getReferenceIds(name);
		return ids != null ? container.getObjectMap(ids) : null;
	}
	
	/**
	 * Internal helper to get the IDs referred to by a
	 * property as an array of ObjexIDs
	 * 
	 * @param name
	 * @return
	 */
	private ObjexID[] getReferenceIds(String name) {
		Object ids = getProperty(name);
		
		ObjexID[] ret = null;
		
		// Direct set of ObjexIDs
		if( ids instanceof ObjexID[] ) {
			ret = (ObjexID[])ids;
		}
		
		// Array of strings
		else if( ids instanceof String[] ) {
			// TODO: Use container to turn each into ObjexIDs
			ret = null;
		}
		
		// Collection of IDs or Strings
		else if( ids instanceof Collection<?> ) {
			List<ObjexID> realIds = null;
			Iterator<?> it = ((Collection<?>)ids).iterator();
			while( it.hasNext() ) {
				Object id = it.next();
				ObjexID realId = null;
				if( id instanceof ObjexID ) realId = (ObjexID)id;
				else if( id instanceof String ) realId = null; // TODO: Use container!
				
				if( realId != null ) {
					if( realIds == null ) realIds = new ArrayList<ObjexID>();
					realIds.add(realId);
				}
			}
			
			ret = realIds != null ? realIds.toArray(new ObjexID[realIds.size()]) : null;
		}
		
		return ret;
	}
	
	/**
	 * Internal helper to clone the state object which is
	 * done if we are not in an open {@link EditableContainer}.
	 * Default uses reflect to create a new instance of the
	 * state object and copy in every parameter.
	 * 
	 * <p>Derived class can override as neccessary</p>
	 * 
	 * @return The cloned version
	 */
	protected Object cloneState() {
		Object ret = strategy.getNewStateInstance();
		
		BeanWrapper copyWrapper = new BeanWrapperImpl(ret);
		BeanWrapper wrapper = new BeanWrapperImpl(state);
		PropertyDescriptor[] props = wrapper.getPropertyDescriptors();
		for( int i = 0 ; i < props.length ; i++ ) {
			if( copyWrapper.isWritableProperty(props[i].getName()) ) {
				copyWrapper.setPropertyValue(
						props[i].getName(), 
						wrapper.getPropertyValue(props[i].getName()));
			}
		}
		
		return ret;
	}
}
