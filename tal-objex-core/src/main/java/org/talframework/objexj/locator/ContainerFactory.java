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
package org.talframework.objexj.locator;

import org.talframework.objexj.Container;


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
