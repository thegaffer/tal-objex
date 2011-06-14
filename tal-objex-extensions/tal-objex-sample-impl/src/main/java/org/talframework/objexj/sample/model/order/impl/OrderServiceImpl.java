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
package org.talframework.objexj.sample.model.order.impl;

import org.talframework.objexj.Container;
import org.talframework.objexj.locator.ContainerFactory;
import org.talframework.objexj.sample.api.order.OrderSummary;
import org.talframework.objexj.sample.api.repository.OrderRepository;
import org.talframework.objexj.sample.api.repository.OrderService;

/**
 * This class implements the order service
 * 
 * @author Tom Spencer
 */
public class OrderServiceImpl implements OrderService {
	
	/** Holds the Objex factory for the order container type */
	private final ContainerFactory locator;
	
	public OrderServiceImpl(ContainerFactory locator) {
        this.locator = locator;
    }
    
    /**
	 * Simply gets the container from the container factory
	 * and wraps it inside a new {@link OrderRepositoryImpl}
	 * instance.
	 */
	public OrderRepository getRepository(String id) {
		Container container = locator.get(id);
		if( container == null ) throw new IllegalArgumentException("Container does not exist: " + id);
		return new OrderRepositoryImpl(container);
	}
	
	/**
	 * Simply gets the container from the container factory
	 * and wraps it inside a new {@link OrderRepositoryImpl}
	 * instance. 
	 */
	public OrderRepository getOpenRepository(String id) {
		Container container = locator.open(id);
		if( container == null ) throw new IllegalArgumentException("Container or transaction does not exist: " + id);
		return new OrderRepositoryImpl(container);
	}
	
	public OrderRepository createNewOrder() {
	    Container container = locator.create();
	    
	    return new OrderRepositoryImpl(container);
	}
	
	/**
	 * TODO: Implement search across docs using doc store object
	 */
	public OrderSummary[] findOrdersByAccount(String accountId) {
	    throw new UnsupportedOperationException("Search across docs not yet implemented");
	}
	
	/**
	 * TODO: Implement search across docs
	 */
	public OrderSummary[] findOrdersByStockItem(String stockItemId) {
	    throw new UnsupportedOperationException("Search across docs not yet implemented");
	}
}
