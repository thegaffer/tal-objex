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
