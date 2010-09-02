package org.tpspencer.tal.objexj.locator;

import org.tpspencer.tal.objexj.Container;

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
     * Creates a new container of this type.
     * 
     * @return The open container (the root object and any defaults have been set)
     */
    public Container create();
    
    /**
	 * Gets a container if this type given its ID. If the
	 * ID represents a transaction then the transaction is
	 * returned.
	 * 
	 * @param id The ID of the container (or transaction)
	 * @return The container
	 */
	public Container get(String id);
	
	/**
	 * Either opens a container for editing for the first
	 * time or re-opens a previously suspended transaction 
	 * as determined by the type of ID.
	 * 
	 * @param id The ID of the container or transaction to open
	 * @return The opened container
	 */
	public Container open(String id);
	
	/**
	 * This method will create a store if it does not
	 * already exist. Stores are not typically created
	 * as a result of any individual user interaction
	 * and are expected to be present. This method will
	 * create the store if (and only if) it does not
	 * already exist.
	 */
	public void createStore();
}
