package org.tpspencer.tal.objexj.locator;

import org.tpspencer.tal.objexj.Container;
import org.tpspencer.tal.objexj.EditableContainer;

/**
 * This interface represents something that create container
 * instances. Each instance operates only for a single
 * type of container. Container factory instances are plugged
 * in to the container factory
 * 
 * @author Tom Spencer
 */
public interface ContainerFactory {
	
	/**
	 * Gets a container if this type given its ID.
	 * 
	 * @param id The ID of the container
	 * @return The container
	 */
	public Container get(String id);
	
	/**
	 * Creates a new container of this type with the
	 * given ID. Note that the container may not exist
	 * in the relevant data store until saved.
	 * 
	 * @param id The ID for the container.
	 * @return The open container (the root object and any defaults have been set)
	 */
	public EditableContainer create(String id);
	
	/**
	 * Opens a container for editing.
	 * 
	 * @param id The ID of the container to open
	 * @return The opened container
	 */
	public EditableContainer open(String id);
	
	/**
	 * Obtains a previously opened container that has
	 * been suspended. Note that if a creation was
	 * previously terminated the container may not exist
	 * in the data store, but the transaction must exist.
	 *  
	 * @param id The ID of the container to open
	 * @param transactionId The existing transaction
	 * @return The opened container
	 */
	public EditableContainer getTransaction(String id, String transactionId);
}
