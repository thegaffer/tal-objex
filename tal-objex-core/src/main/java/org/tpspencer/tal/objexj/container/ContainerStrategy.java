package org.tpspencer.tal.objexj.container;

import java.util.Map;

import org.tpspencer.tal.objexj.object.ObjectStrategy;

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
	 * @return The name of the root object type
	 */
	public String getRootObjectName();
	
	/**
	 * Call to get the object strategy for a type of object.
	 * 
	 * @param name The name of the object we want strategy for
	 * @return The strategy for the given object
	 */
	public ObjectStrategy getObjectStrategy(String name);
	
	/**
	 * @return The map of all object strategies of allowed objects in the container
	 */
	public Map<String, ObjectStrategy> getObjectStrategies();
}
