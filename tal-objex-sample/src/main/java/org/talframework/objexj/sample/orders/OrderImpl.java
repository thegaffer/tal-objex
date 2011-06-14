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
package org.talframework.objexj.sample.orders;

import java.util.List;

/**
 * This class is our Order business object implementation in the sample.
 *
 * @author Tom Spencer
 */
public class OrderImpl implements Order {

    private long account;
    private List<OrderItem> items;
    
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#getAccount()
     */
    @Override
    public long getAccount() {
        return account;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#setAccount(long)
     */
    @Override
    public void setAccount(long account) {
        this.account = account;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#getItems()
     */
    @Override
    public List<OrderItem> getItems() {
        return items;
    }
    /* (non-Javadoc)
     * @see org.talframework.objexj.sample.orders.impl.Order#setItems(java.util.List)
     */
    @Override
    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
