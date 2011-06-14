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
package org.talframework.objexj.sample.api.repository;

import org.talframework.objexj.sample.api.order.OrderSummary;

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
	 * Opens an order repository given its ID or re-gets a 
	 * suspended transaction (depending on ID).
	 * 
	 * @param transactionId The ID of the transaction
	 * @return The repository
	 */
	public OrderRepository getOpenRepository(String id);
	
	/**
	 * Call to create a new order. The order is not actually
	 * saved until persisted (at which point it will get an
	 * ID).
	 * 
	 * @return The order
	 */
	public OrderRepository createNewOrder();
	
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
