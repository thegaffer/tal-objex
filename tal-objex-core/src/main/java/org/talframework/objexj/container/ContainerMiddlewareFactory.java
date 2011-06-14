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
package org.talframework.objexj.container;

/**
 * This interface represents a class that can create 
 * ContainerMiddleware instances.
 * 
 * @author Tom Spencer
 */
public interface ContainerMiddlewareFactory {
    
    /**
     * Called to get a new {@link ContainerMiddleware} instance for
     * a new container of the given type. The container will not be 
     * persisted as a result of this call.
     * 
     * @param strategy The strategy for the container
     * @return A new transaction middleware for the editable container to use
     */
    public ContainerMiddleware createContainer(ContainerStrategy strategy);
    
    /**
	 * Called to get a new {@link ContainerMiddleware} instance for
	 * the given container.
	 * 
	 * @param strategy The strategy for the container
	 * @param id The ID of the container in question
	 * @return A new middleware for a container to use
	 */
	public ContainerMiddleware getMiddleware(ContainerStrategy strategy, String id);
	
	/**
	 * Called to get an existing transaction middleware.
	 * 
	 * @param strategy The strategy for the container
     * @param id The ID of the existing transaction or container for a new transaction
	 * @return A new transaction middleware
	 */
	public ContainerMiddleware getTransaction(ContainerStrategy strategy, String transactionId);
}
