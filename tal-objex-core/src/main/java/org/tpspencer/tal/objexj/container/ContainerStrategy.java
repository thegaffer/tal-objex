package org.tpspencer.tal.objexj.container;

import java.util.List;

import org.tpspencer.tal.objexj.ObjexID;
import org.tpspencer.tal.objexj.events.EventHandler;
import org.tpspencer.tal.objexj.events.EventListener;
import org.tpspencer.tal.objexj.exceptions.EventHandlerNotFoundException;
import org.tpspencer.tal.objexj.exceptions.ObjectTypeNotFoundException;
import org.tpspencer.tal.objexj.exceptions.QueryNotFoundException;
import org.tpspencer.tal.objexj.object.ObjectStrategy;
import org.tpspencer.tal.objexj.query.Query;

/**
 * This interface represents a strategy for a container.
 * Classes that implement this contain all the details that
 * a (generic) container requires to work with a specific
 * custom container type. This tells any generic container
 * about all of the objects inside the container and how
 * to create them and their state counterparts.
 * 
 * @author Tom Spencer
 */
public interface ContainerStrategy {
	
	/**
	 * @return The name of the container type
	 */
	public String getContainerName();
	
	/**
	 * @return The ID of the container if it is a fixed container (i.e. a store)
	 */
	public String getContainerId();
	
	/**
	 * @return The name of the root object type
	 */
	public String getRootObjectName();
	
	/**
	 * @return The ID of the root object which is always fixed
	 */
	public ObjexID getRootObjectID();
	
	/**
	 * Call to get the object strategy for a type of object.
	 * 
	 * @param type The name of the object we want strategy for
	 * @return The strategy for the given object
	 * @throws ObjectTypeNotFoundException If the type if not known
	 */
	public ObjectStrategy getObjectStrategy(String type);
	
	/**
     * Call to get the object strategy for a type of object 
     * given its state class type.
     * 
     * FUTURE: This is not currently needed, leaving for now, but we may want to remove
     * 
     * @param stateType The type of state object we want the strategy for
     * @return The strategy for the given object
     * @throws ObjectTypeNotFoundException If the type if not known
     */
    public ObjectStrategy getObjectStrategyForState(String stateType);
    
    /**
     * Call to get a named query for the container.
     * 
     * @param name The name of the query
     * @return The query
     * @throws QueryNotFoundException If the query is not known
     */
    public Query getQuery(String name);
    
    /**
     * Call to get a named event handler for the container.
     * 
     * @param name The name of the event
     * @return The event handler
     * @throws EventHandlerNotFoundException If the event name is not known
     */
    public EventHandler getEventHandler(String name);
    
    /**
     * Call to get all standard listeners for this container.
     * These listeners are always applied to the container.
     * 
     * FUTURE: Should we support calculated listeners from container??
     * 
     * @return The standard event listeners.
     */
    public List<EventListener> getStandardListeners();
}
