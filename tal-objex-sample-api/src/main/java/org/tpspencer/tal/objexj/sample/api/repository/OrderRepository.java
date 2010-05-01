package org.tpspencer.tal.objexj.sample.api.repository;

import org.tpspencer.tal.objexj.sample.api.order.Order;
import org.tpspencer.tal.objexj.sample.api.order.OrderSummary;

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
	 * Call to get an order by its ID. With this method
	 * you can get the order out for read-only purposes or
	 * you can get it out for editing.
	 * 
	 * @param id The ID of the order
	 * @param toEdit Determines if the order returned is for editing
	 * @return The order
	 */
	public Order findOrderById(String id, boolean toEdit);
	
	/**
	 * Call to search for all orders using a particular 
	 * stock item. This call returns only read-only order
	 * summaries as you can only edit 1 order at a time.
	 * 
	 * @param stockItemId
	 * @return
	 */
	public OrderSummary[] findOrdersByStockItem(String stockItemId);
	
	/**
	 * Call to search for all orders using a particular 
	 * account id. This call returns only read-only order
	 * summaries as you can only edit 1 order at a time.
	 * 
	 * @param stockItemId
	 * @return
	 */
	public OrderSummary[] findOrdersByAccount(String accountId);
	
	/**
	 * Call to persist an order previously obtained via
	 * a call to findOrderById given its ID and marked
	 * for edit that has subsequently been changed.
	 * 
	 * @param order The order to persist
	 */
	public void persistOrder(Order order);
}
