package org.tpspencer.tal.objexj.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.object.ObjectStrategy;

/**
 * Standard class that implements the container interface
 * using as many plugin interfaces as possible so it is
 * reusable in many situations.
 * 
 * @author Tom Spencer
 */
public class StandardContainer implements Container {

	/** Holds the ID of the container */
	private final String id;
	
	/** Holds the strategy for this container */
	private final ContainerStrategy strategy;
	/** Holds the runtime middleware for this container */
	private final ContainerMiddleware middleware;
	
	/**
	 * Standard constructor for the container.
	 * 
	 * @param strategy The general strategy for the container
	 * @param idStrategy The specific runtime ID strategy to use
	 * @param objectLoader The specific runtime object loader
	 * @param id The ID of the container
	 * @param cache The cache to use (if any)
	 */
	public StandardContainer(
			ContainerStrategy strategy, 
			ContainerMiddleware middleware,
			String id) {
		Assert.notNull(strategy, "The container strategy must be present");
		Assert.notNull(middleware, "The container middleware must be present");
		
		this.strategy = strategy;
		this.middleware = middleware;
		this.id = id;
		
		middleware.init(this);
	}
		
	/**
	 * Returns the ID
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Returns the strategies name
	 */
	public String getType() {
		return strategy.getContainerName();
	}
	
	/**
	 * @return The container strategy
	 */
	public ContainerStrategy getContainerStrategy() {
		return strategy;
	}
	
	/**
	 * @return The middlware for the container
	 */
	public ContainerMiddleware getMiddleware() {
		return middleware;
	}
	
	public ObjexObj getObject(Object id) {
		ObjexID realId = middleware.convertId(id);
		String type = middleware.getObjectType(realId);
		ObjectStrategy objectStrategy = strategy.getObjectStrategies().get(type);
		Object state = middleware.loadObject(objectStrategy.getStateClass(), realId);
		return createObjexObj(realId, state);
	}
	
	public <T> Object getObject(Object id, Class<T> expected) {
		ObjexObj obj = getObject(id);
		// TODO: Convert object into correct type, behaviour!
		return null;
	}
	
	public Collection<ObjexObj> getObjects(ObjexID[] ids) {
		Collection<ObjexObj> ret = null;
		for( int i = 0 ; i < ids.length ; i++ ) {
			ObjexObj obj = getObject(ids[i]);
			if( obj != null ) {
				if( ret == null ) ret = new ArrayList<ObjexObj>();
				ret.add(obj);
			}
		}

		return ret;
	}
	
	public Map<ObjexID, ObjexObj> getObjectMap(ObjexID[] ids) {
		Map<ObjexID, ObjexObj> ret = null;
		for( int i = 0 ; i < ids.length ; i++ ) {
			ObjexObj obj = getObject(ids[i]);
			if( obj != null ) {
				if( ret == null ) ret = new HashMap<ObjexID, ObjexObj>();
				ret.put(obj.getId(), obj);
			}
		}

		return ret;
	}
	
	public EditableContainer openContainer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Helper to actually return the ObjexObj instance. This instance
	 * is formed from details on the objects strategy.
	 * 
	 * @param id The ID of the object
	 * @param state The state object
	 * @return The ObjexObj instance
	 */
	protected ObjexObj createObjexObj(ObjexID id, Object state) {
		if( state == null ) return null;
		
		ContainerStrategy strategy = getContainerStrategy();
		ObjectStrategy objectStrategy = strategy.getObjectStrategies().get(middleware.getObjectType(id));
		if( objectStrategy == null ) throw new IllegalArgumentException("Container [" + objectStrategy + "] is not configured to use the type of state object passed in: " + id); 
		
		// Extract parent if there is one
		ObjexID realParentId = null;
		if( objectStrategy.getParentIdProp() != null ) {
			BeanWrapper wrapper = new BeanWrapperImpl(state);
			if( wrapper.isReadableProperty(objectStrategy.getParentIdProp()) ) {
				Object parentId = wrapper.getPropertyValue(objectStrategy.getParentIdProp());
				realParentId = middleware.convertId(parentId);
			}
		}
		
		return objectStrategy.getObjexObjInstance(this, realParentId, id, state);
	}
}
