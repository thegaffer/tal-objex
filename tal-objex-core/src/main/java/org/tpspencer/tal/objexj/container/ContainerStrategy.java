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
	 * @return The map of all object strategies of allowed objects in the container
	 */
	public Map<String, ObjectStrategy> getObjectStrategies();
}
