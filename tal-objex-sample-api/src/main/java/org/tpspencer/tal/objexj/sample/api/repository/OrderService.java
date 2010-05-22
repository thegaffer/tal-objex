package org.tpspencer.tal.objexj.sample.api.repository;

import org.tpspencer.tal.objexj.sample.api.order.OrderSummary;

/**
 * This interface provides access to an order repository
 * given the order you wish to act upon. It also provides
 * some separation in case you have opened up a transaction
 * for changes and want to make further changes.
 * 
 * @author Tom Spencer
 */
public interface OrderService {

	/**
	 * Gets the order repository given its ID
	 * 
	 * @param id The ID of the order
	 * @return The order repository
	 */
	public OrderRepository getRepository(String id);
	
	/**
	 * Gets a previously opened order repository 
	 * 
	 * @param id The ID of the repository
	 * @param transactionId The ID of the transaction upob that repository
	 * @return The repository
	 */
	public OrderRepository getOpenRepository(String id, String transactionId);
	
	/**
	 * Call to create a new order. The order is not actually
	 * saved until persisted.
	 * 
	 * @param id The id of the new order
	 * @return The order
	 */
	public OrderRepository createNewOrder(String id);
	
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
}
