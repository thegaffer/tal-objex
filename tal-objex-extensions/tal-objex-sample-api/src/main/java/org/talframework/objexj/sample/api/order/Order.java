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
package org.talframework.objexj.sample.api.order;

import java.util.List;

/**
 * The business interface to an order
 * 
 * @author Tom Spencer
 */
public interface Order {
    
    /**
     * @return the account
     */
    public abstract long getAccount();

    /**
     * @param account the account to set
     */
    public abstract void setAccount(long account);

    /**
     * @return A reference to each item in the order
     */
    public abstract List<OrderItem> getItems();
    
    /**
     * @return A reference to each item in the order
     */
    public abstract List<String> getItemRefs();
    
    public abstract OrderItem createItem();
	
	public abstract void removeItemById(Object id);
}
