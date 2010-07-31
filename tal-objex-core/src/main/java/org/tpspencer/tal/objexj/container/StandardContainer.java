package org.tpspencer.tal.objexj.container;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;
import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.ObjexObj;
import org.tpspencer.tal.objexj.ObjexObjStateBean;
import org.tpspencer.tal.objexj.object.DefaultObjexID;
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
	private String id;
	
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
	 * @param id The ID of the container (set after a save)
	 */
	protected void setId(String id) {
	    if( this.id != null && !id.equals(this.id) ) throw new IllegalArgumentException("Cannot change a containers id [" + this.id + "]: " + id);
	    this.id = id;
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
	
	public ObjexObj getRootObject() {
	    ObjexID rootId = strategy.getRootObjectID();
	    return getObject(rootId);
	}
	
	public ObjexID getObjectId(Object id) {
	    return DefaultObjexID.getId(id);
	}
	
	public ObjexObj getObject(Object id) {
	    if( id == null ) return null; // Just protect from a stupid call!!
	    
		ObjexID realId = DefaultObjexID.getId(id);
		if( realId == null ) throw new IllegalArgumentException("ID passed in does not appear to be valid: " + id);
		
		ObjectStrategy objectStrategy = strategy.getObjectStrategy(realId.getType());
		Object state = middleware.loadObject(objectStrategy.getStateClass(), realId);
		
		if( state instanceof ObjexObjStateBean ) {
		    return createObjexObj(objectStrategy, realId, (ObjexObjStateBean)state);
		}
		else if( state instanceof ObjexObj ) {
		    return (ObjexObj)state;
		}
		else if( state != null ) {
		    throw new IllegalArgumentException("The simple container only supports ObjexObjStateBean or direct ObjexObj instances from middleware: " + state);
		}
		else {
		    // TODO: Or should this be Object not found??
		    return null;
		}
	}
	
	public <T> T getObject(Object id, Class<T> expected) {
		ObjexObj obj = getObject(id);
		return obj != null ? obj.getBehaviour(expected) : null;
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
		// TODO: Remove when no longer deprecated and removed
		return null;
	}
	
	/**
	 * Helper to actually return the ObjexObj instance. This instance
	 * is formed from details on the objects strategy.
	 * 
	 * @param objectStrategy The strategy for the object we want
	 * @param id The ID of the object
	 * @param state The state object
	 * @return The ObjexObj instance
	 */
	protected ObjexObj createObjexObj(ObjectStrategy objectStrategy, ObjexID id, ObjexObjStateBean state) {
		if( state == null ) return null;
		
		ObjexID parentId = DefaultObjexID.getId(state.getParentId());
		return objectStrategy.getObjexObjInstance(this, parentId, id, state);
	}
}
