/*
 * Copyright 2009 Thomas Spencer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
