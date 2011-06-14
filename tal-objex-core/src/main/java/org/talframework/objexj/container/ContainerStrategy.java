/**
 * Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>
 *
 * This file is part of Objex <http://www.tpspencer.com/site/objexj/>
 *
 * Objex is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Objex is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Objex. If not, see <http://www.gnu.org/licenses/>.
 *
 * Note on dates: Objex was first conceived in 1997. The Java version
 * first started in 2004. Year in copyright notice is the year this
 * version was built. Code was created at various points between these
 * two years.
 */
package org.talframework.objexj.container;

import java.util.Collection;
import java.util.List;

import org.talframework.objexj.ObjexID;
import org.talframework.objexj.events.EventHandler;
import org.talframework.objexj.events.EventListener;
import org.talframework.objexj.exceptions.EventHandlerNotFoundException;
import org.talframework.objexj.exceptions.ObjectTypeNotFoundException;
import org.talframework.objexj.exceptions.QueryNotFoundException;
import org.talframework.objexj.query.Query;

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
	 * @return The type names of all objects in the container
	 */
	public Collection<String> getObjectNames();
	
	/**
	 * Call to get the object strategy for a type of object.
	 * 
	 * @param type The name of the object we want strategy for
	 * @return The strategy for the given object
	 * @throws ObjectTypeNotFoundException If the type if not known
	 */
	public ObjectStrategy getObjectStrategy(String type);
	
	/**
	 * Call to see if we have an {@link ObjectStrategy} for the
	 * given object. This is typically done to see if we can deduce
	 * an ID from the object.
	 * 
	 * <p>Note: No exception thrown here if not found.</p>
	 * 
	 * @param object The object to get strategy for
	 * @return The {@link ObjectStrategy} or null if none is found
	 */
	public ObjectStrategy getObjectStrategyForObject(Object object);
	
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
