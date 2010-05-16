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
	 * from any other. The container will be created if it does
	 * not exist when completed.
	 * 
	 * @param id The ID of the container
	 * @param expectExists Will fail if the container does not already exist
	 * @return The {@link EditableContainer}
	 */
	public EditableContainer open(String id, boolean expectExists);
}
