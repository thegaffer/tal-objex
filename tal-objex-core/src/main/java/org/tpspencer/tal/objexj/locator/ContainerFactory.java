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
	 * Call to create a brand new container of the
	 * given type. The container is immediately created
	 * as Editable.
	 * 
	 * @param type The type of container to create
	 * @param rootObject The persistable state of the root object
	 * @return The EditableContainer
	 */
	public EditableContainer create(String type, Object rootObject);

	/**
	 * Gets a container if this type given its ID.
	 * 
	 * @param id The ID of the container
	 * @return The container
	 */
	public Container get(String id);
	
	/**
	 * Reopens the transaction on the specified container.
	 * 
	 * @param id The ID of the container
	 * @param transactionId The ID of the already open transaction
	 * @return The {@link EditableContainer}
	 */
	public EditableContainer get(String id, String transactionId);
	
	/**
	 * Opens a transaction on the container given by the ID.
	 * This transaction represents a brand new isolated transaction
	 * from any other. 
	 * 
	 * @param id The ID of the container
	 * @return The {@link EditableContainer}
	 */
	public EditableContainer open(String id);
}
