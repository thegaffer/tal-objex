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

import org.talframework.objexj.sample.api.stock.Product;

public interface OrderItem {

    /**
     * @return Reference to the stock item (if any)
     */
    public abstract Product getStockItem();
    
    /**
     * @param item the item to set
     */
    public abstract void setStockItem(Product item);

    /**
     * @return the ref
     */
    public abstract String getRef();
    
    /**
     * @param ref the ref to set
     */
    public abstract void setRef(String ref);

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * @param name the name to set
     */
    public abstract void setName(String name);

    /**
     * @return the description
     */
    public abstract String getDescription();

    /**
     * @param description the description to set
     */
    public abstract void setDescription(String description);

    /**
     * @return the quantity
     */
    public abstract double getQuantity();

    /**
     * @param quantity the quantity to set
     */
    public abstract void setQuantity(double quantity);

    /**
     * @return the measure
     */
    public abstract String getMeasure();

    /**
     * @param measure the measure to set
     */
    public abstract void setMeasure(String measure);

    /**
     * @return the price
     */
    public abstract double getPrice();

    /**
     * @param price the price to set
     */
    public abstract void setPrice(double price);

    /**
     * @return the currency
     */
    public abstract String getCurrency();

    /**
     * @param currency the currency to set
     */
    public abstract void setCurrency(String currency); 
}
