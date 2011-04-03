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

import org.talframework.objexj.sample.api.order.Order;
import org.talframework.objexj.sample.api.order.OrderItem;

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
     * @return The ID of the order this repository represents
     */
    public String getId();

	/**
	 * @return The order object
	 */
	public Order getOrder();
	
	/**
	 * 
	 * @param id The ID of the item we want
	 * @return The requested order item
	 */
	public OrderItem getOrderItem(String id);
	
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
