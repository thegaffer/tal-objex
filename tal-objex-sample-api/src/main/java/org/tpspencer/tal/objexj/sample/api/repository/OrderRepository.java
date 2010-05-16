package org.tpspencer.tal.objexj.sample.api.repository;

import org.tpspencer.tal.objexj.sample.api.order.Order;

/**
 * This interface represents the repository of orders.
 * Services can come here to get at order objects.
 * 
 * <p>This interface is part of the sample Objex project.
 * This demonstrates how to design your interfaces using 
 * general good practice without exposing any Objex
 * elements. We will then implement this using Objex.</p> 
 * 
 * @author Tom Spencer
 */
public interface OrderRepository {

	/**
	 * @return The order object
	 */
	public Order getOrder();
	
	/**
	 * Call to open up the repository for changes. Any
	 * objects obtained from the repository cannot be
	 * edited with re-getting them.
	 */
	public void open();
	
	/**
	 * Call to suspend all changes currently made and
	 * come back to them later 
	 * 
	 * @return The ID of the transaction
	 */
	public String suspend();
	
	/**
	 * Call to persist all changes made on this repository.
	 */
	public void persist();
}
