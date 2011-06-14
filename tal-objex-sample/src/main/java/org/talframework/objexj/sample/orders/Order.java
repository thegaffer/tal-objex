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
 * This interface represents an Order in our sample project. This interface
 * is exposed to the outside world. Also differences between this interface
 * and our implementation object tell Objex how to treat the object.
 *
 * @author Tom Spencer
 */
public interface Order {

    /**
     * @return the account
     */
    public abstract long getAccount();

    /**
     * Setter for the account field
     *
     * @param account the account to set
     */
    public abstract void setAccount(long account);

    /**
     * @return the items
     */
    public abstract List<OrderItem> getItems();

    /**
     * Setter for the items field
     *
     * @param items the items to set
     */
    public abstract void setItems(List<OrderItem> items);

}