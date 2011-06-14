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
package org.talframework.objexj.sample.stock;

public interface Product {

    /**
     * @return the id
     */
    public abstract String getId();

    /**
     * Setter for the id field
     *
     * @param id the id to set
     */
    public abstract void setId(String id);

    /**
     * @return the name
     */
    public abstract String getName();

    /**
     * Setter for the name field
     *
     * @param name the name to set
     */
    public abstract void setName(String name);

    /**
     * @return the description
     */
    public abstract String getDescription();

    /**
     * Setter for the description field
     *
     * @param description the description to set
     */
    public abstract void setDescription(String description);

    /**
     * @return the price
     */
    public abstract double getPrice();

    /**
     * Setter for the price field
     *
     * @param price the price to set
     */
    public abstract void setPrice(double price);

    /**
     * @return the currency
     */
    public abstract String getCurrency();

    /**
     * Setter for the currency field
     *
     * @param currency the currency to set
     */
    public abstract void setCurrency(String currency);

    /**
     * @return the nearestProduct
     */
    public Product getNearestProduct();

    /**
     * Setter for the nearestProduct field
     *
     * @param nearestProduct the nearestProduct to set
     */
    public void setNearestProduct(Product nearestProduct);
}